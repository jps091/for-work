package project.forwork.api.domain.thumbnailimage.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.common.infrastructure.enums.FieldType;

import java.util.Optional;

public interface ThumbnailImageJpaRepository extends JpaRepository<ThumbnailImageEntity, Long> {
    Optional<ThumbnailImageEntity> findByFieldType(FieldType field);
}
