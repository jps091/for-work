package project.forwork.api.domain.orderresume.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.service.port.MailSender;
import project.forwork.api.domain.orderresume.service.port.OrderResumeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderResumeService {

    private final OrderResumeRepository orderResumeRepository;
    private final MailSender mailSender;
}
