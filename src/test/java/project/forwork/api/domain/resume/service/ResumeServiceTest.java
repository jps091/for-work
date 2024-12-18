package project.forwork.api.domain.resume.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.model.ResumeDecision;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.mock.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResumeServiceTest {

    private ResumeService resumeService;
    private FakeResumeRepository fakeResumeRepository;
    private FakeSalesPostRepository fakeSalesPostRepository;
    private FakeS3Service fakeS3Service;
    private FakeCartResumeRepository fakeCartResumeRepository;

    private FakeUserRepository fakeUserRepository;

    @Mock
    private ResumePageService resumePageService;

    @BeforeEach
    void init(){
        fakeUserRepository = new FakeUserRepository();
        fakeResumeRepository = new FakeResumeRepository();
        fakeSalesPostRepository = new FakeSalesPostRepository();
        fakeS3Service = new FakeS3Service();
        fakeCartResumeRepository = new FakeCartResumeRepository();
        FakeResumeDecisionRepository fakeResumeDecisionRepository = new FakeResumeDecisionRepository();
        this.resumeService = ResumeService.builder()
                .resumeRepository(fakeResumeRepository)
                .userRepository(fakeUserRepository)
                .cartResumeRepository(fakeCartResumeRepository)
                .salesPostRepository(fakeSalesPostRepository)
                .s3Service(fakeS3Service)
                .resumePageService(resumePageService)
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("user1@naver.com")
                .name("user1")
                .password("123")
                .status(UserStatus.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@naver.com")
                .name("user2")
                .password("123")
                .status(UserStatus.USER)
                .build();

        User admin = User.builder()
                .id(3L)
                .email("admin@naver.com")
                .name("admin")
                .password("321")
                .status(UserStatus.ADMIN)
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
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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

        SalesPost salesPost1 = SalesPost.builder()
                .id(1L)
                .resume(resume1)
                .salesStatus(SalesStatus.SELLING)
                .build();

        fakeSalesPostRepository.save(salesPost1);
    }

    @Test
    void currentUser_ResumeRegisterRequest_MultipartFile_을_가지고_Resume을_등록할_수_있다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeRegisterRequest body = ResumeRegisterRequest.builder()
                .field(FieldType.AI)
                .level(LevelType.NEW)
                .resumeUrl("www.naver.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();

        MultipartFile descriptionImage = new TestMultipartFile("image", "test.jpg", "image/jpeg", "Dummy Image Content".getBytes());

        //when(상황발생)
        Resume resume = resumeService.register(currentUser, body, descriptionImage);

        //then(검증)
        assertThat(resume.getId()).isNotNull();
        assertThat(resume.getField()).isEqualTo(FieldType.AI);
        assertThat(resume.getDescription()).isEqualTo("test resume");
        assertThat(resume.getDescriptionImageUrl()).isEqualTo("http://localhost/fake-bucket/1a2a3a4a5.jpg");
    }

    @Test
    void 가격이_10만원이_넘는_Resume을_등록할_수_없다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeRegisterRequest request = ResumeRegisterRequest.builder()
                .field(FieldType.AI)
                .level(LevelType.NEW)
                .resumeUrl("www.naver.com")
                .price(new BigDecimal("150000.00"))
                .description("test resume")
                .build();
        MultipartFile descriptionImage = new TestMultipartFile("image", "test.jpg", "image/jpeg", "Dummy Image Content".getBytes());

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> resumeService.register(currentUser, request, descriptionImage))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void CurrentUser_ResumeModifyRequest를_가지고_자신이_작성한_Resume만_수정할_수_있고_Resume의_상태는_PENDING으로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeModifyRequest request = ResumeModifyRequest.builder()
                .field(FieldType.DEVOPS)
                .level(LevelType.SENIOR)
                .resumeUrl("www.naver.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();
        MultipartFile descriptionImage = new TestMultipartFile("image", "test.jpg", "image/jpeg", "Dummy Image Content".getBytes());

        // when
        resumeService.modify(1L, currentUser, request, descriptionImage);
        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);

        // then
        assertThat(resume.getField()).isEqualTo(FieldType.DEVOPS);
        assertThat(resume.getStatus()).isEqualTo(ResumeStatus.PENDING);
    }

    @Test
    void 이미_판매중인_이력서를_수정_한다면_판매글_상태가_CANCELED_로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeModifyRequest request = ResumeModifyRequest.builder()
                .field(FieldType.DEVOPS)
                .level(LevelType.SENIOR)
                .resumeUrl("www.naver.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();
        MultipartFile descriptionImage = new TestMultipartFile("image", "test.jpg", "image/jpeg", "Dummy Image Content".getBytes());

        //when
        resumeService.modify(1L, currentUser, request, descriptionImage);
        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);

        // then
        SalesPost salesPost = fakeSalesPostRepository.getByResumeIdWithThrow(resume.getId());
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.CANCELED);
    }

    @Test
    void CurrentUser_ResumeModifyRequest를_가지고_다른_사람이_작성한_Resume는_수정할_수_없다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();
        ResumeModifyRequest request = ResumeModifyRequest.builder()
                .field(FieldType.DEVOPS)
                .level(LevelType.SENIOR)
                .resumeUrl("www.naver.com")
                .price(new BigDecimal("70000.00"))
                .description("test resume")
                .build();
        MultipartFile descriptionImage = new TestMultipartFile("image", "test.jpg", "image/jpeg", "Dummy Image Content".getBytes());

        // then
        assertThatThrownBy(() -> resumeService.modify(2L, currentUser, request, descriptionImage))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void 이력서를_삭제_하면_상태가_DELETE로_변경_된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        // when
        resumeService.delete(1L, currentUser);

        // then
        Resume resume = fakeResumeRepository.getByIdWithThrow(1L);
        assertThat(resume.getStatus()).isEqualTo(ResumeStatus.DELETE);
    }

    @Test
    void 이력서를_삭제_하면_판매글은_삭제된다(){
        //given(상황환경 세팅)
        CurrentUser currentUser = CurrentUser.builder()
                .id(1L)
                .build();

        // when
        resumeService.delete(1L, currentUser);

        // then
        assertThatThrownBy(() -> fakeSalesPostRepository.getByIdWithThrow(1L))
                .isInstanceOf(ApiException.class);
    }
}