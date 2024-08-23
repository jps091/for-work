package project.forwork.api.common.service.port;

public interface MailSender {
    void send(String email, String title, String content);
}
