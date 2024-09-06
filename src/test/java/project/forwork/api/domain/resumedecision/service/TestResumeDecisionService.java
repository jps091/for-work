/*
package project.forwork.api.domain.resumedecision.service;

import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;

public class TestResumeDecisionService extends ResumeDecisionService{

    public TestResumeDecisionService(ResumeDecisionRepository resumeDecisionRepository,
                                     ResumeRepository resumeRepository,
                                     UserRepository userRepository) {
        super(resumeDecisionRepository, resumeRepository, userRepository);
    }
    public Resume getResumeWithThrow(Long resumeId) {

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .roleType(RoleType.USER)
                .build();


        // 테스트 시, 특정 이력서 ID를 기반으로 가짜 데이터를 반환하거나, 특정 로직을 수행합니다.
        return Resume.builder()
                .id(3L)
                .seller(user1)
                .field(FieldType.BACKEND)
                .level(LevelType.SENIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume3")
                .status(ResumeStatus.PENDING)
                .build();
    }
}
*/
