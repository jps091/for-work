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
    private final String description;
    private final ResumeStatus status;
    private final LocalDateTime modifiedAt;


    public static Resume from(User user, ResumeRegisterRequest body){
        validPrice(body.getPrice());
        return Resume.builder()
                .seller(user)
                .field(body.getField())
                .level(body.getLevel())
                //.resumeUrl(resumeRegisterRequest.getResumeUrl()) TODO Test
                //.descriptionImageUrl(resumeRegisterRequest.getdescriptionImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(body.getPrice())
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume modifyResumePending(ResumeModifyRequest body){
        validPrice(body.getPrice());
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(body.getField())
                .level(body.getLevel())
                //.resumeUrl(request.getResumeUrl()) TODO Test
                //.descriptionImageUrl(request.getdescriptionImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
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
                //.resumeUrl(request.getResumeUrl()) TODO Test
                //.descriptionImageUrl(request.getdescriptionImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .descriptionImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(price)
                .description(description)
                .status(status)
                .build();
    }

    public String createSalesPostTitle(){
        return level.getDescription() + " " + field.getDescription() + " 이력서 #" + getId();
    }

    public boolean isAuthorMismatch(Long sellerId){
        log.info("sellerId={}, sellerId={}",seller.getId(), sellerId);
        return !Objects.equals(seller.getId(), sellerId);
    }

    public boolean isActiveMismatch(){
        return status != ResumeStatus.ACTIVE;
    }

    private static void validPrice(BigDecimal price) {
        if(price.compareTo(new BigDecimal("100000")) > 0){
            throw new ApiException(ResumeErrorCode.PRICE_NOT_VALID);
        }
    }
}
