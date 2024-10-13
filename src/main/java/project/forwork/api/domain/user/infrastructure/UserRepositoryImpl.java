package project.forwork.api.domain.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(UserEntity.from(user));
    }

    @Override
    public User getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND, id));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }
}
