package project.forwork.api.domain.user.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        log.info("call save");
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public User saveAndFlush(User user) {
        log.info("call save");
        return userJpaRepository.saveAndFlush(UserEntity.from(user)).toModel();
    }

    @Override
    public User getById(long id) {
        return findById(id).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }
}
