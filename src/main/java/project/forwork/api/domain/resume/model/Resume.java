package project.forwork.api.domain.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class Resume {

    private final Long id;
    private final User seller;
    private final FieldType field;
    private final LevelType level;
    private final String resumeUrl;
    private final String descriptionImageUrl;
    private final BigDecimal price;
    private Integer salesQuantity;
    private final String description;
    private final ResumeStatus status;
    private final LocalDateTime modifiedAt;


    public static Resume from(User user, ResumeRegisterRequest body, String descriptionUrl){
        if(body.getPrice().compareTo(new BigDecimal("100000")) > 0){
            throw new ApiException(ResumeErrorCode.PRICE_NOT_VALID);
        }
        return Resume.builder()
                .seller(user)
                .field(body.getField())
                .level(body.getLevel())
                .resumeUrl(body.getResumeUrl())
                .descriptionImageUrl(descriptionUrl)
                .salesQuantity(0)
                //.resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")TODO Test
                //.descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(body.getPrice())
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume modify(ResumeModifyRequest body, String descriptionUrl){
        validPrice(body.getPrice());
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(body.getField())
                .level(body.getLevel())
                .resumeUrl(body.getResumeUrl())
                .descriptionImageUrl(descriptionUrl)
                //.resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")TODO Test
                //.descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(body.getPrice())
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume updateStatus(ResumeStatus status){
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(field)
                .level(level)
                .resumeUrl(resumeUrl)
                .descriptionImageUrl(descriptionImageUrl)
                .price(price)
                .salesQuantity(salesQuantity)
                .description(description)
                .status(status)
                .build();
    }

    public Resume increaseSalesQuantity(){
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(field)
                .level(level)
                .resumeUrl(resumeUrl)
                .descriptionImageUrl(descriptionImageUrl)
                .price(price)
                .salesQuantity(salesQuantity + 1)
                .description(description)
                .status(status)
                .build();
    }

    public String createSalesPostTitle(){
        return level.getDescription() + " " + field.getDescription() + " 이력서 #" + getId();
    }

    public boolean isAuthorMismatch(Long sellerId){
        return !Objects.equals(seller.getId(), sellerId);
    }

    public boolean isActiveMismatch(){
        return status != ResumeStatus.ACTIVE;
    }

    public void validPrice(BigDecimal price) {

    }
}
