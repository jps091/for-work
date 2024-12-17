package project.forwork.api.domain.user.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.producer.Producer;
import project.forwork.api.domain.user.infrastructure.message.TempPasswordMessage;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Builder
@RequiredArgsConstructor
public class PasswordInitializationService {


    private final UserRepository userRepository;
    private final UuidHolder uuidHolder;
    private final Producer producer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void issueTemporaryPassword(User user) {
        User tempUser = user.initTemporaryPassword(uuidHolder.random());
        userRepository.save(tempUser);
        TempPasswordMessage message = TempPasswordMessage.from(tempUser.getEmail(), tempUser.getPassword());
        producer.sendPasswordMail(message);
    }
}
