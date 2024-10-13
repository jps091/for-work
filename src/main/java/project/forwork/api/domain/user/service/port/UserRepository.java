package project.forwork.api.domain.user.service.port;

import project.forwork.api.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    void delete(User user);
    User getByIdWithThrow(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
