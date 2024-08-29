package project.forwork.api.domain.field;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import project.forwork.api.common.infrastructure.BaseTimeEntity;


@Entity
@Table(name = "fields")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FieldEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "field")
    private FieldType fieldType;
}
