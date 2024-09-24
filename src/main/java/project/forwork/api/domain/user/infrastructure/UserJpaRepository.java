package project.forwork.api.domain.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndName(String email, String name);
}
