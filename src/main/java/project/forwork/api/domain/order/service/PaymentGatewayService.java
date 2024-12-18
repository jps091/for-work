package project.forwork.api.domain.order.service;

import io.netty.channel.ConnectTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import project.forwork.api.domain.order.infrastructure.model.ConfirmPaymentDto;
import project.forwork.api.domain.order.infrastructure.model.PaymentFullCancelDto;
import project.forwork.api.domain.order.infrastructure.model.PaymentPartialCancelDto;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayService {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final String SECRET = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

    @Value("${pg.url}")
    public String URL;

    @Retryable(
            value = {SocketTimeoutException.class, ConnectTimeoutException.class},
            maxAttempts = 1,
            backoff =  @Backoff(delay = 2000)
    )
    public void confirm(ConfirmPaymentDto body){
        String authorizations = getAuthorizations();
        String confirmURL = URL +"confirm";

        RestClient defaultClient = createRestClient();

        defaultClient.post()
                .uri(confirmURL)
                .headers(httpHeaders -> {
                    httpHeaders.add("Authorization", authorizations);
                    httpHeaders.add("Content-Type", "application/json");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
    }

    public void cancelFullPayment(String paymentKey, PaymentFullCancelDto body) {
        cancelPayment(paymentKey, body, "전체");
    }

    public void cancelPartialPayment(String paymentKey, PaymentPartialCancelDto body) {
        cancelPayment(paymentKey, body, "부분");
    }

    @Retryable(
            value = {SocketTimeoutException.class, ConnectTimeoutException.class},
            maxAttempts = 1,
            backoff =  @Backoff(delay = 2000)
    )
    public void cancelPayment(String paymentKey, Object cancelRequestBody, String cancelType) {
        String authorizations = getAuthorizations();
        String cancelURL = cancelUrl(paymentKey);

        RestClient defaultClient = createRestClient();

        defaultClient.post()
                .uri(cancelURL)
                .headers(httpHeaders -> {
                    httpHeaders.add("Authorization", authorizations);
                    httpHeaders.add("Content-Type", "application/json");
                })
                .contentType(MediaType.APPLICATION_JSON)
                .body(cancelRequestBody)
                .retrieve()
                .toEntity(Object.class);

        log.info("{} 결제 취소 성공: paymentKey = {}", cancelType, paymentKey);
    }

    private static RestClient createRestClient() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withReadTimeout(Duration.ofSeconds(3))
                .withConnectTimeout(Duration.ofSeconds(3));

        ClientHttpRequestFactory factory = ClientHttpRequestFactories.get(settings);
        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }

    private String getAuthorizations() {
        byte[] encodedBytes = encoder.encode((SECRET + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    private String cancelUrl(String paymentKey){
        return URL + paymentKey + "/cancel";
    }
}
