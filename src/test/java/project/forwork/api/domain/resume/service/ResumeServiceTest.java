package project.forwork.api.domain.resume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.FakeResumeDecisionRepository;
import project.forwork.api.mock.FakeResumeRepository;
import project.forwork.api.mock.FakeUserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResumeServiceTest {

    private ResumeService resumeService;

    @BeforeEach
    void init(){
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeResumeRepository fakeResumeRepository = new FakeResumeRepository();
        FakeResumeDecisionRepository fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        this.resumeService = ResumeService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .resumeDecisionRepository(fakeResumeDecisionRepository)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .roleType(RoleType.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@naver.com")
                .name("user2")
                .password("123")
                .roleType(RoleType.USER)
                .build();

        User admin = User.builder()
                .id(3L)
                .email("admin@naver.com")
                .name("admin")
                .password("321")
                .roleType(RoleType.ADMIN)
                .build();
        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);
        fakeUserRepository.save(admin);

        Resume resume1 = Resume.builder()
                .id(1L)
                .seller(user1)
                .field(FieldType.AI)
                .level(LevelType.JUNIOR)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("10000.00"))
                .description("test resume1")
                .status(ResumeStatus.ACTIVE)
                .build();

        Resume resume2 = Resume.builder()
                .id(2L)
                .seller(user2)
                .field(FieldType.BACKEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("98000.00"))
                .description("test resume2")
                .status(ResumeStatus.PENDING)
                .build();

        Resume resume3 = Resume.builder()
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

        Resume resume4 = Resume.builder()
                .id(4L)
                .seller(user2)
                .field(FieldType.FRONTEND)
                .level(LevelType.NEW)
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(new BigDecimal("70000.00"))
                .description("test resume4")
                .status(ResumeStatus.REJECTED)
                .build();

        fakeResumeRepository.save(resume1);
        fakeResumeRepository.save(resume2);
        fakeResumeRepository.save(resume3);
        fakeResumeRepository.save(resume4);

        ResumeDecision resumeDecision1 = ResumeDecision.builder()
                .id(1L)
                .admin(admin)
                .decisionStatus(DecisionStatus.PENDING)
                .registeredAt(LocalDateTime.of(2024, 9, 5, 12, 0, 0))
                .resume(resume1)
                .build();

        ResumeDecision resumeDecision2 = ResumeDecision.builder()
                .id(2L)
                .admin(admin)
                .decisionStatus(DecisionStatus.PENDING)
                .registeredAt(LocalDateTime.of(2024, 9, 5, 12, 0, 0))
                .resume(resume2)
                .build();

        fakeResumeDecisionRepository.save(resumeDecision1);
        fakeResumeDecisionRepository.save(resumeDecision2);
    }

    @Test
    void currentUser_ResumeRegisterRequest를_가지고_Resume을_등록할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeRegisterRequest request = ResumeRegisterRequest.builder()
                .field(FieldType.AI)
                .level(LevelType.NEW)
                .resumeUrl("www.naver.com")
                .architectureImageUrl("www.google.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();

        //when(상황발생)
        Resume resume = resumeService.register(currentUser, request);

        //then(검증)
        assertThat(resume.getId()).isNotNull();
        assertThat(resume.getField()).isEqualTo(FieldType.AI);
        assertThat(resume.getDescription()).isEqualTo("test resume");
    }

    @Test
    void CurrentUser_ResumeModifyRequest를_가지고_자신이_작성한_PENDING상태인_Resume만_수정할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeModifyRequest request = ResumeModifyRequest.builder()
                .field(FieldType.DEVOPS)
                .level(LevelType.SENIOR)
                .resumeUrl("www.naver.com")
                .architectureImageUrl("www.google.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();

        // 자신이 작성한 대기 상태인 이력서 수정하는 경우
        resumeService.modifyIfPending(3L, currentUser, request); // 성공
        Resume resume = resumeService.getByIdWithThrow(3L);
        assertThat(resume.getField()).isEqualTo(FieldType.DEVOPS);

        // 자신이 작성 했지만 대기 상태가 아닌 이력서 수정하는 경우
        assertThatThrownBy(() -> resumeService.modifyIfPending(1L, currentUser, request))
                .isInstanceOf(ApiException.class);

        // 다른사람이 작성한 대기 상태인 이력서 수정하는 경우
        assertThatThrownBy(() -> resumeService.modifyIfPending(2L, currentUser, request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 자신이_작성한_이력서상태를_대기상태로_변경할_수있다() {
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        //when
        resumeService.updatePending(1L, currentUser);

        //then 자신이 작성한 경우
        Resume resume = resumeService.getByIdWithThrow(1L);
        assertThat(resume.getStatus()).isEqualTo(ResumeStatus.PENDING);

        //자신이 작성하지 않은 경우
        assertThatThrownBy(() -> resumeService.updatePending(2L, currentUser))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 자신이_요청한_이력서를_전체_삭제_할_수있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        //when(상황발생)
        resumeService.deleteByUser(currentUser.getId());
        //then(검증)
        assertThat(resumeService.findAll().size()).isEqualTo(2);
    }
}