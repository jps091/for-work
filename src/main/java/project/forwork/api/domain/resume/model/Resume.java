package project.forwork.api.domain.resume.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Resume {

    private final Long id;
    private final User seller;
    private final FieldType field;
    private final LevelType level;
    private final String resumeUrl;
    private final String architectureImageUrl;
    private final BigDecimal price;
    private final String description;
    private final ResumeStatus status;

    @Builder
    public Resume(Long id, User seller, FieldType field, LevelType level, String resumeUrl, String architectureImageUrl, BigDecimal price, String description, ResumeStatus status) {
        this.id = id;
        this.seller = seller;
        this.field = field;
        this.level = level;
        this.resumeUrl = resumeUrl;
        this.architectureImageUrl = architectureImageUrl;
        this.price = price;
        this.description = description;
        this.status = status;
    }

    public static Resume from(User user, ResumeRegisterRequest resumeRegisterRequest){
        return Resume.builder()
                .seller(user)
                .field(resumeRegisterRequest.getField())
                .level(resumeRegisterRequest.getLevel())
                //.resumeUrl(resumeRegisterRequest.getResumeUrl()) TODO Test
                //.architectureImageUrl(resumeRegisterRequest.getArchitectureImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(resumeRegisterRequest.getPrice())
                .description(resumeRegisterRequest.getDescription())
                .status(ResumeStatus.PENDING)
                .build();
    }

    public Resume modify(ResumeModifyRequest request){
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(request.getField())
                .level(request.getLevel())
                //.resumeUrl(request.getResumeUrl()) TODO Test
                //.architectureImageUrl(request.getArchitectureImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(request.getPrice())
                .description(request.getDescription())
                .status(status)
                .build();
    }

    public Resume updateStatus(ResumeStatus status){
        return Resume.builder()
                .id(id)
                .seller(seller)
                .field(field)
                .level(level)
                //.resumeUrl(request.getResumeUrl()) TODO Test
                //.architectureImageUrl(request.getArchitectureImageUrl())
                .resumeUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .architectureImageUrl("http://docs.google.com/presentation/d/1AT954aQPzBf0vm47yYqDDfGtbkejSmJ9/edit")
                .price(price)
                .description(description)
                .status(status)
                .build();
    }


    public boolean isAuthorMismatch(Long sellerId){
        return !Objects.equals(seller.getId(), sellerId);
    }
}
