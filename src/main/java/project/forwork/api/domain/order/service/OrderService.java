package project.forwork.api.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.cartresume.model.CartResume;
import project.forwork.api.domain.cartresume.service.port.CartResumeRepository;
import project.forwork.api.domain.order.controller.model.CancelRequest;
import project.forwork.api.domain.order.controller.model.OrderRequest;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
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
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;
    private final MailSender mailSender;

    public Order orderNow(CurrentUser currentUser, Long resumeId){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

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
        List<OrderResume> orderResumes = cartResumes.stream()
                .map(cartResume -> OrderResume.create(order, cartResume.getResume()))
                .toList();

        Order savedOrder = orderRepository.save(order);
        orderResumes.forEach(orderResumeRepository::save);

        return savedOrder;
    }
}
