package project.forwork.api.domain.salespost.model;

import org.junit.jupiter.api.Test;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.thumbnailimage.model.ThumbnailImage;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SalesPostTest {

    @Test
    void Resume_과_ThumbnailImage_로_Selling_상태인_판매글을_생성할_수_있다(){
        //given(상황환경 세팅)
        Resume resume = Resume.builder()
                .id(1L)
                .build();

        ThumbnailImage thumbnailImage = ThumbnailImage.builder()
                .id(1L)
                .build();

        //when(상황발생)
        SalesPost salesPost = SalesPost.create(resume, thumbnailImage);

        //then(검증)
        assertThat(salesPost).isNotNull();
        assertThat(salesPost.getSalesStatus()).isEqualTo(SalesStatus.SELLING);
        assertThat(salesPost.getResume()).isEqualTo(resume);
        assertThat(salesPost.getThumbnailImage()).isEqualTo(thumbnailImage);
    }

    @Test
    void 판매글은_상태를_변경할_수_있다(){
        //given(상황환경 세팅)
        SalesPost salesPost = SalesPost.builder()
                .id(1L)
                .salesStatus(SalesStatus.SELLING)
                .build();

        //when(상황발생)
        SalesPost changedSalesPost = salesPost.changeStatus(SalesStatus.CANCELED);

        //then(검증)
        assertThat(changedSalesPost.getSalesStatus()).isEqualTo(SalesStatus.CANCELED);
    }
}