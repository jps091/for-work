package project.forwork.api.mock;

import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<User> data = new ArrayList<>();


    @Override
    public User save(User user) {
        if(user.getId() == null || user.getId() == 0){
            User newUser = User.builder()
                    .id(id.incrementAndGet())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .status(user.getStatus())
                    .build();
            data.add(newUser);
            return newUser;
        }else{
            data.removeIf(u -> Objects.equals(u.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }

    @Override
    public void delete(User user) {
        data.removeIf(u -> Objects.equals(u.getId(), user.getId()));
    }

    @Override
    public User getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Optional<User> findById(Long id) {
        return data.stream().filter(u -> u.getId().equals(id)).findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.stream().filter(u -> u.getEmail().equals(email)).findAny();
    }

    @Override
    public boolean existsByEmail(String email) {
        return data.stream().anyMatch(u -> u.getEmail().equals(email));
    }
}