package project.forwork.api.domain.user.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.infrastructure.Producer;
import project.forwork.api.common.infrastructure.message.TempPasswordMessage;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.common.service.port.UuidHolder;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@Service
@Builder
@RequiredArgsConstructor
public class PasswordInitializationService {


    private final UserRepository userRepository;
    private final UuidHolder uuidHolder;
    private final MailSender mailSender;
    private final Producer producer;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void issueTemporaryPassword(User user) {
        user = user.initTemporaryPassword(uuidHolder.random());
        userRepository.save(user);
        TempPasswordMessage message = TempPasswordMessage.from(user.getEmail(), user.getPassword());
        producer.sendTempPasswordMail(message);
        //sendPassword(user.getEmail(), user.getPassword());
    }

    private void sendPassword(String email, String password){
        String title = "for-work 임시 비밀번호 발급";
        String content = "임시 비밀번호 : " + password;
        mailSender.send(email, title, content);
    }
}
