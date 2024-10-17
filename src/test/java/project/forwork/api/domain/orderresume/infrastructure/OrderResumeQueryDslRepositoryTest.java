package project.forwork.api.domain.orderresume.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.domain.orderresume.controller.model.OrderResumeResponse;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.controller.model.PurchaseResponse;
import project.forwork.api.domain.orderresume.model.OrderResume;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:repository-custom-test.yml")
@DataJpaTest
@Import({OrderResumeRepositoryCustomImpl.class, OrderResumeRepositoryImpl.class})
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

    @Autowired
    private OrderResumeRepositoryImpl orderResumeRepository;

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void findPurchaseResume_메서드는_orderResumes_들로_PurchaseInfo_들을_반환_한다(long orderResumeId){
        //given(상황환경 세팅)
        OrderResume orderResume1 = orderResumeRepository.getByIdWithThrow(1L);
        OrderResume orderResume2 = orderResumeRepository.getByIdWithThrow(2L);
        OrderResume orderResume3 = orderResumeRepository.getByIdWithThrow(3L);
        OrderResume orderResume4 = orderResumeRepository.getByIdWithThrow(4L);
        List<OrderResume> orderResumes = List.of(orderResume1, orderResume2, orderResume3);

        //when(상황발생)
        List<PurchaseResponse> content = orderResumeRepositoryCustom.findAllPurchaseResume(orderResumes);

        //then(검증)
        assertThat(content.size()).isEqualTo(3);
        Long orderId = content.get((int) orderResumeId - 1).getOrderId();
        OrderResume orderResume = orderResumeRepository.getByIdWithThrow(orderResumeId);
        assertThat(orderId).isEqualTo(orderResume.getOrder().getId());
    }

    @Test
    void findByOrderId_메서드는_ORDER_ID_로_ORDER_에_속한_모든_OrderResumeResponse_을_반환_한다(){
        //given(상황환경 세팅)
        //when(상황발생)
        List<OrderResumeResponse> orderResumeResponses = orderResumeRepositoryCustom.findByOrderId(8L);

        //then(검증)
        assertThat(orderResumeResponses).isNotEmpty();
        assertThat(orderResumeResponses.size()).isEqualTo(2);
        assertThat(orderResumeResponses.get(0).getTitle()).isEqualTo("NEW BACKEND 이력서 #4");
    }
}