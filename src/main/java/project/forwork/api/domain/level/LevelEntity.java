package project.forwork.api.domain.level;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;


@Entity
@Table(name = "levels")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "level")
    private LevelType levelType;
}
