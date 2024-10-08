package project.forwork.api.domain.orderresume.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:repository-custom-test.yml")
@DataJpaTest
@Import({OrderResumeRepositoryCustomImpl.class})
@SqlGroup({
        @Sql(value = "/sql/user-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/order-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/order-resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class OrderResumeQueryDslRepositoryTest {

    @Autowired
    private OrderResumeRepositoryCustomImpl orderResumeRepositoryCustom;

    @Test
    void findPurchaseResume_메서드는_CONFIRM_상태인_OrderResume_들로_PurchaseInfo_들을_반환_한다(){
        //given(상황환경 세팅)

        //when(상황발생)
        List<PurchaseResponse> content = orderResumeRepositoryCustom.findAllPurchaseResume();

        //then(검증)
        assertThat(content.size()).isEqualTo(1);
    }

    @Test
    void findByUserIdAndStatus_메서드는_USER_ID와_OrderOrderResumeStatus_들로_OrderResumeResponse_들을_반환_한다(){
        //given(상황환경 세팅)
        List<OrderResumeStatus> statuses = List.of(OrderResumeStatus.ORDERED, OrderResumeStatus.CONFIRM, OrderResumeStatus.SENT);

        //when(상황발생)
        List<OrderResumeResponse> orderResumeResponses = orderResumeRepositoryCustom.findByUserIdAndStatus(1L, statuses);

        //then(검증)
        assertThat(orderResumeResponses).isNotEmpty();

        for (OrderResumeResponse response : orderResumeResponses) {
            assertThat(statuses).contains(response.getStatus());
        }
    }

    @Test
    void findByOrderId_메서드는_ORDER_ID_로_ORDER_에_속한_모든_OrderResumeResponse_을_반환_한다(){
        //given(상황환경 세팅)
        //when(상황발생)
        List<OrderResumeResponse> orderResumeResponses = orderResumeRepositoryCustom.findByOrderId(8L);

        //then(검증)
        assertThat(orderResumeResponses).isNotEmpty();
        assertThat(orderResumeResponses.size()).isEqualTo(2);
        assertThat(orderResumeResponses).allMatch(or -> Objects.equals(or.getOrderId(), 8L));
        assertThat(orderResumeResponses.get(0).getTitle()).isEqualTo("NEW BACKEND 이력서 #4");
    }
}