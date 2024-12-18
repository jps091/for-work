package project.forwork.api.common.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BaseTimeEntity {

    @Column(updatable = false, name = "registered_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime registeredAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        registeredAt = now;
        modifiedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        modifiedAt = LocalDateTime.now().withSecond(0).withNano(0);
    }
}