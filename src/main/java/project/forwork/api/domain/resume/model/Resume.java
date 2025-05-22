package project.forwork.api.domain.resume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.forwork.api.common.domain.CurrentUser;
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
    private final Long sellerId;
    private final String sellerEmail;
    private final FieldType field;
    private final LevelType level;
    private final String resumeUrl;
    private final String descriptionImageUrl;
    private final BigDecimal price;
    private final Integer salesQuantity;
    private final String description;
    private final ResumeStatus status;
    private final LocalDateTime registeredAt;


    public static Resume from(CurrentUser user, ResumeRegisterRequest body, String descriptionUrl){
        validateResumePrice(body.getPrice());
        return Resume.builder()
                .sellerId(user.getId())
                .sellerEmail(user.getEmail())
                .field(body.getField())
                .level(body.getLevel())
                .resumeUrl(body.getResumeUrl())
                .descriptionImageUrl(descriptionUrl)
                .salesQuantity(0)
                .price(body.getPrice())
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public static Resume from(CurrentUser user, ResumeRegisterRequest body){
        validateResumePrice(body.getPrice());
        return Resume.builder()
                .sellerId(user.getId())
                .sellerEmail(user.getEmail())
                .field(body.getField())
                .level(body.getLevel())
                .resumeUrl(body.getResumeUrl())
                .salesQuantity(0)
                .price(body.getPrice())
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume callbackDescriptionImageUrl(String filePath){
        return Resume.builder()
                .id(id)
                .sellerId(sellerId)
                .sellerEmail(sellerEmail)
                .field(field)
                .level(level)
                .resumeUrl(resumeUrl)
                .descriptionImageUrl(filePath)
                .price(price)
                .salesQuantity(salesQuantity)
                .description(description)
                .status(status)
                .build();
    }

    public Resume modify(Long userId, ResumeModifyRequest body, String newUrl){
        validateAuthor(userId);
        validateResumePrice(body.getPrice());

        if(newUrl == null){
            newUrl = descriptionImageUrl;
        }
        return Resume.builder()
                .id(id)
                .sellerId(sellerId)
                .sellerEmail(sellerEmail)
                .field(body.getField())
                .level(body.getLevel())
                .resumeUrl(body.getResumeUrl())
                .descriptionImageUrl(newUrl)
                .price(body.getPrice())
                .salesQuantity(salesQuantity)
                .description(body.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume updateStatus(ResumeStatus status){
        return Resume.builder()
                .id(id)
                .sellerId(sellerId)
                .sellerEmail(sellerEmail)
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

    public Resume delete(Long userId){
        validateAuthor(userId);
        return Resume.builder()
                .id(id)
                .sellerId(sellerId)
                .sellerEmail(sellerEmail)
                .field(field)
                .level(level)
                .resumeUrl(resumeUrl)
                .descriptionImageUrl(descriptionImageUrl)
                .price(price)
                .salesQuantity(salesQuantity)
                .description(description)
                .status(ResumeStatus.DELETE)
                .build();
    }

    public Resume increaseSalesQuantity(){
        return Resume.builder()
                .id(id)
                .sellerId(sellerId)
                .sellerEmail(sellerEmail)
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

    public String createTitle(){
        return field.toString() + " " + level.toString() + " 이력서 #" + getId();
    }

    public boolean isAuthorMismatch(Long sellerId){
        return !Objects.equals(this.sellerId, sellerId);
    }

    private static void validateResumePrice(BigDecimal price) {
        if(price.compareTo(new BigDecimal("100000")) > 0 ||
                price.compareTo(new BigDecimal("10000")) < 0){
            throw new ApiException(ResumeErrorCode.PRICE_NOT_VALID);
        }
    }

    private void validateAuthor(Long userId) {
        if(isAuthorMismatch(userId)){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }

    public boolean isActiveMismatch(){
        return status != ResumeStatus.ACTIVE;
    }

    public String getDescriptionSummary() {
        if(description.length() < 15){
            return description;
        }
        return description.substring(0, 15) + "...";
    }
}
