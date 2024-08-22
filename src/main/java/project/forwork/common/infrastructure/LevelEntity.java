package project.forwork.common.infrastructure;

import jakarta.persistence.*;
import project.forwork.common.infrastructure.enums.FieldType;

import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.enums.LevelType;


@Entity
@Table(name = "levels")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "level")
    private LevelType levelType;
}
