package project.forwork.api.domain.user.service.port;

import project.forwork.api.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    void delete(User user);
    User getByIdWithThrow(long id);
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
}
