package project.forwork.api.domain.order.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.OrderErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.order.controller.model.PartialCancelRequest;
import project.forwork.api.domain.order.controller.model.OrderDetailResponse;
import project.forwork.api.domain.order.controller.model.OrderInCartRequest;
import project.forwork.api.domain.order.controller.model.OrderResponse;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.OrderResumeService;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepositoryCustom;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
@Slf4j
/***
 * 1. 주문 (바로구매 / 장바구니구매)
 * 2. 주문 상태 변경 (확정, 취소, 부분취소) 확정 : 30분뒤 자동확정 or 즉시확정기능
 * * 문제점 : 부분취소시 구매확정을 어떻게 자동으로 할것인지, 또한 확정시 메일 전송
 *            부분취소시 선택한 이력서만 상태를 취소로 변경하고 주문상태는 부분취소로 변경
 *
 * 3. 주문 내역 조회 (진행중)
 * 4. 구매 내역 조회 (구매확정)
 * 5. 취소 내역 조회 (구매취소)
 * 6. 결제 하기 (추후 구현)
 * 7. 쿠폰 적용 하기 (추구 구현)
 *
 * 주문 요청 : 사용자가 주문을 생성하고 해당 orderId를 멱등키로 활용 / REQUESTED
 * 결제(PG API) : 결제 요청(멱등키 포함) -> 결제 API 결제 요청 콜 -> 성공 or 실패
 * 승인(for-work 결제 시스템) : 결제가 성공하면 주문 상태를 변경(결제완료) / 실패(주문취소 or 서버에서 결제 재시도 로직 시도) / CONFIRM
 * 주문 확정 : 결제 승인 완료시 주문 상태를 확정으로 변경 및 이메일 전송, 결제 데이터 생성
 *
 * 주문 서비스 : 주문상태 REQUESTED 변경 -> 결제 서비스 승인 요청 (API CONFIRM)
 * 결제 서비스 : PG 승인 요청 -> 결제 기록 저장
 *
 * String orderId = order.getId() + "-" + UUID.randomUUID().toString();
 */
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartResumeRepository cartResumeRepository;
    private final OrderResumeRepository orderResumeRepository;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;
    private final UuidHolder uuidHolder;
    private final OrderResumeService orderResumeService;
    private final SalesPostRepository salesPostRepository;

    public Order orderNow(CurrentUser currentUser, Long salesPostId){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        SalesPost salesPost = salesPostRepository.getByIdWithThrow(salesPostId);
        Resume resume = salesPost.getResume();

        String requestId = createRequestId(user.getId());
        if (orderRepository.existsByRequestId(requestId)) {
            throw new ApiException(OrderErrorCode.ORDER_ALREADY_REQUEST);
        }

        Order order = Order.create(user, resume, requestId, clockHolder);
        order = orderRepository.save(order);

        OrderResume orderResume = OrderResume.create(order, resume);
        orderResumeRepository.save(orderResume);

        return order;
    }

    public Order orderInCart(CurrentUser currentUser, OrderInCartRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        List<CartResume> cartResumes = body.getCartResumeIds().stream()
                .map(cartResumeRepository::getByIdWithThrow)
                .toList();

        String requestId = createRequestId(user.getId());
        if (orderRepository.existsByRequestId(requestId)) {
            throw new ApiException(OrderErrorCode.ORDER_ALREADY_REQUEST);
        }

        Order order = Order.create(user, cartResumes, requestId, clockHolder);
        order = orderRepository.save(order);

        orderResumeService.registerByCartResume(order, cartResumes);

        cartResumeRepository.delete(cartResumes);

        return order;
    }

    public void orderConfirmNow(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.orderConfirmNow(currentUser.getId(), clockHolder);
        orderRepository.save(order);

        orderResumeService.sendMailForConfirmedOrder(order);
    }

    public void cancelOrder(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.cancelOrder(currentUser.getId(), clockHolder);
        orderRepository.save(order);
        orderResumeService.cancel(order);
    }

    public void cancelPartialOrder(CurrentUser currentUser, Long orderId, PartialCancelRequest body){
        List<OrderResume> orderResumes = orderResumeService.cancelPartialOrderResumes(body.getOrderResumeIds());

        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.cancelPartialOrder(currentUser.getId(), orderResumes, clockHolder);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(CurrentUser currentUser){
        return orderRepository.findByUserId(currentUser.getId()).stream()
                .map(order -> {
                    List<OrderResumeResponse> orderResumeResponses = orderResumeRepositoryCustom.findByOrderId(order.getId());
                    String orderResumeTitle = orderResumeResponses.get(0).getTitle();
                    String orderTitle = orderResumeResponses.size() == 1 ? orderResumeTitle : orderResumeTitle + "...";
                    return OrderResponse.from(order, orderTitle);
                }).toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getOrderWithThrow(currentUser.getId(), orderId);
        List<OrderResumeResponse> orderResumes = orderResumeRepositoryCustom.findByOrderId(order.getId());

        return OrderDetailResponse.builder()
                .email(order.getBuyerEmail())
                .totalPrice(order.getTotalAmount())
                .orderedAt(order.getOrderedAt())
                .orderId(order.getId())
                .orderResumeResponses(orderResumes)
                .build();
    }

    private String createRequestId(Long userId){
        return clockHolder.millis() / 5000 + "-" + uuidHolder.random() + "-" + userId;
    }
}