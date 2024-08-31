package project.forwork.api.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordInitializationService {


    private final UserRepository userRepository;
    private final UuidHolder uuidHolder;
    private final MailSender mailSender;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void issueTemporaryPassword(User user) {
        user = user.initTemporaryPassword(uuidHolder.random());
        userRepository.save(user);

        sendPassword(user.getEmail(), user.getPassword());
    }

    private void sendPassword(String email, String password){
        String title = "for-work 임시 비밀번호 발급";
        String content = "임시 비밀번호 : " + password;
        mailSender.send(email, title, content);
    }
}
