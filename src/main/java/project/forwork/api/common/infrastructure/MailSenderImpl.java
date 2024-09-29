package project.forwork.api.common.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.forwork.api.common.service.port.MailSender;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final JavaMailSender javaMailSender;

    @Async // TODO 공부필요 1. 톰캣의 쓰레드, 스프링 쓰레드 별개의 것 2. 하지만 둘다 JVM 리소스 사용
    @Override
    public void send(String email, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
    }
}
