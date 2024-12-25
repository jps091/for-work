package project.forwork.api.domain.resumedecision.model;

import org.junit.jupiter.api.Test;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ResumeDecisionTest {

    @Test
    void ResumeDecision의_approve는_관리지와_이력서를_통해할_수_있다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .status(ResumeStatus.PENDING)
                .build();

        User admin = User.builder()
                .id(1L)
                .status(UserStatus.ADMIN)
                .build();

        //when(상황발생)
        ResumeDecision resumeDecision = ResumeDecision.approve(admin, resume);

        //then(검증)
        assertThat(resumeDecision).isNotNull();
        assertThat(resumeDecision.getResume()).isEqualTo(resume);
        assertThat(resumeDecision.getAdmin()).isEqualTo(admin);
        assertThat(resumeDecision.getDecisionStatus()).isEqualTo(DecisionStatus.APPROVE);
    }

    @Test
    void ResumeDecision의_approve는_일반유저로는_예외가_발생한다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .status(ResumeStatus.PENDING)
                .build();

        User user = User.builder()
                .id(1L)
                .status(UserStatus.USER)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> ResumeDecision.approve(user, resume))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void ResumeDecision의_deny는_관리지와_이력서를_통해할_수_있다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .status(ResumeStatus.PENDING)
                .build();

        User admin = User.builder()
                .id(1L)
                .status(UserStatus.ADMIN)
                .build();

        //when(상황발생)
        ResumeDecision resumeDecision = ResumeDecision.deny(admin, resume);

        //then(검증)
        assertThat(resumeDecision).isNotNull();
        assertThat(resumeDecision.getResume()).isEqualTo(resume);
        assertThat(resumeDecision.getAdmin()).isEqualTo(admin);
        assertThat(resumeDecision.getDecisionStatus()).isEqualTo(DecisionStatus.DENY);
    }

    @Test
    void ResumeDecision의_deny는_일반유저로는_예외가_발생한다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .status(ResumeStatus.PENDING)
                .build();

        User user = User.builder()
                .id(1L)
                .status(UserStatus.USER)
                .build();

        //when(상황발생)
        //then(검증)
        assertThatThrownBy(() -> ResumeDecision.deny(user, resume))
                .isInstanceOf(ApiException.class);
    }
}