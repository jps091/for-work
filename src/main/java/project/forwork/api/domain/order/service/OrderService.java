package project.forwork.api.domain.order.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.order.controller.model.CancelRequest;
import project.forwork.api.domain.order.controller.model.OrderDetailResponse;
import project.forwork.api.domain.order.controller.model.OrderRequest;
import project.forwork.api.domain.order.controller.model.OrderResponse;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
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
 */
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartResumeRepository cartResumeRepository;
    private final OrderResumeRepository orderResumeRepository;
    private final OrderResumeRepositoryCustom orderResumeRepositoryCustom;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;
    private final OrderResumeService orderResumeService;
    private final SalesPostRepository salesPostRepository;

    public Order orderNow(CurrentUser currentUser, Long salesPostId){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        SalesPost salesPost = salesPostRepository.getByIdWithThrow(salesPostId);
        Resume resume = salesPost.getResume();

        Order order = Order.create(user, resume, clockHolder);
        order = orderRepository.save(order);

        OrderResume orderResume = OrderResume.create(order, resume);
        orderResumeRepository.save(orderResume);

        return order;
    }

    public Order orderInCart(CurrentUser currentUser, OrderRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        List<CartResume> cartResumes = body.getCartResumeIds().stream()
                .map(cartResumeRepository::getByIdWithThrow)
                .toList();

        Order order = Order.create(user, cartResumes, clockHolder);
        order = orderRepository.save(order);

        orderResumeService.registerByCartResume(order, cartResumes);

        cartResumeRepository.delete(cartResumes);

        return order;
    }

    //@Scheduled(fixedRate = 10000, initialDelay = 10000) // TODO 시간 변경
    public void markAsWaiting(){
        updatedOrderStatus(OrderStatus.ORDER, OrderStatus.WAIT);
    }

    //@Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void markPartialAsWaiting(){
        updatedOrderStatus(OrderStatus.PARTIAL_CANCEL, OrderStatus.PARTIAL_WAIT);
    }

    @Scheduled(fixedRate = 10000)//, initialDelay = 10000)
    public void markAsConfirm(){
        updatedOrderStatus(OrderStatus.WAIT, OrderStatus.CONFIRM);
    }

    //@Scheduled(fixedRate = 30000, initialDelay = 30000)
    public void markPartialAsConfirm(){
        updatedOrderStatus(OrderStatus.PARTIAL_WAIT, OrderStatus.PARTIAL_CONFIRM);
    }

    public void confirmOrderNow(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.confirmOrderNow(currentUser.getId(), clockHolder);
        orderRepository.save(order);

        orderResumeService.sendMailForConfirmedOrder(order);
    }

    public void cancelOrder(CurrentUser currentUser, Long orderId){
        Order order = orderRepository.getByIdWithThrow(orderId);
        order = order.cancelOrder(currentUser.getId(), clockHolder);
        orderRepository.save(order);
        orderResumeService.cancel(order);
    }

    public void cancelPartialOrder(CurrentUser currentUser, Long orderId, CancelRequest body){
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
        // 검증로직이 필요하지않은이유 -> currentUser.getId() 자체로 레포에서 조회를 하기때문에
        // currentUser 인터셉터와 쿠키에 의해 만들어진 검증된 객체
        Order order = orderRepository.getOrderWithThrow(currentUser.getId(), orderId);
        List<OrderResumeResponse> orderResumes = orderResumeRepositoryCustom.findByOrderId(order.getId());

        return OrderDetailResponse.builder()
                .email(order.getBuyerEmail())
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getOrderedAt())
                .orderId(order.getId())
                .orderResumeResponses(orderResumes)
                .build();
    }

    public void updatedOrderStatus(OrderStatus oldStatus, OrderStatus updatedStatus) {
        int limit = 2;
        log.info("updatedOrderStatus {} -> {}", oldStatus, updatedStatus);

        while (true) {
            List<Order> orders = orderRepository.findByStatus(oldStatus, limit);

            // 더 이상 처리할 주문이 없으면 반복 종료
            if (orders.isEmpty()) {
                log.info("No more orders with status: {}", oldStatus);
                break;
            }

            // 상태를 업데이트 후 저장
            orders = updateOrdersByStatus(orders, updatedStatus);

            // 상태가 변경된 후에는 다시 조회하지 않음
            sendMailByOrderConfirm(updatedStatus, orders);
        }
    }

    public List<Order> updateOrdersByStatus(List<Order> orders, OrderStatus status) {
        List<Order> updatedOrders = orders.stream()
                .map(order -> order.updateStatus(status))
                .toList();

        orderRepository.saveAll(updatedOrders);
        return updatedOrders;
    }

    public void sendMailByOrderConfirm(OrderStatus updatedStatus, List<Order> orders) {
        if (updatedStatus.equals(OrderStatus.PARTIAL_CONFIRM) || updatedStatus.equals(OrderStatus.CONFIRM)) {
            // 메일 발송 후 상태 업데이트
            orderResumeService.sendMailForConfirmedOrders(orders);

            List<Order> updatedOrders = orders.stream()
                    .map(order -> order.confirmAuto(clockHolder))  // 확정 시간
                    .toList();
            orderRepository.saveAll(updatedOrders);
        }
    }
}