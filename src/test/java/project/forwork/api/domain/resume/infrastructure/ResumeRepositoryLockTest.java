package project.forwork.api.domain.resume.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.user.infrastructure.UserJpaRepository;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ResumeRepositoryLockTest {

    @Autowired
    ResumeJpaRepository resumeRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @BeforeEach
    void init(){
        //given(상황환경 세팅)
        User user = User.builder()
                .id(1L)
                .email("www@naver.com")
                .name("test")
                .password("123")
                .roleType(RoleType.USER)
                .build();
        User newUser = userJpaRepository.save(UserEntity.from(user)).toModel();

        Resume resume = Resume.builder()
                .id(1L)
                .seller(newUser)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("10000.00"))
                .description("test resume1")
                .status(ResumeStatus.ACTIVE)
                .salesQuantity(0)
                .build();

        resumeRepository.save(ResumeEntity.from(resume));
    }

    @Test
    void 동시에_100명이_이력서를_구매하면_판매수량은_100개_이다() throws InterruptedException {
        //given(상황환경 세팅)
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Resume resume = resumeRepository.findById(1L).orElseThrow().toModel();

        //when(상황발생)

        for(int i = 0; i < threadCount; i++){
            executorService.submit(() ->{
                try{
                    Resume newResume = resume.increaseSalesQuantity();
                    resumeRepository.save(ResumeEntity.from(newResume));
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then(검증)
        ResumeEntity resumeEntity = resumeRepository.findById(1L).orElseThrow();
        assertThat(resumeEntity.getSalesQuantity()).isEqualTo(100);
    }
}