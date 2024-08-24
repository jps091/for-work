package project.forwork.api.domain.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.domain.user.infrastructure.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
