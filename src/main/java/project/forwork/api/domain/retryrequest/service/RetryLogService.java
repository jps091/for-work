package project.forwork.api.domain.retryrequest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.forwork.api.domain.order.service.CheckoutService;
import project.forwork.api.domain.retryrequest.infrastructure.RetryLogJpaRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryLogService {

    private final RetryLogJpaRepository retryLogJpaRepository;

    private final CheckoutService checkoutService;
    private final ObjectMapper objectMapper;


}
