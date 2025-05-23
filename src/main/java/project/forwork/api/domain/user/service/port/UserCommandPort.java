package project.forwork.api.domain.user.service.port;

import project.forwork.api.domain.user.model.User;

import java.util.Optional;

public interface UserCommandPort {
    User register(User user);
    User update(User user);
    void delete(User user);
}
