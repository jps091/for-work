package project.forwork.api.common.config.rabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueueType {
    VERIFY("verify.queue"),
    USER_BUYER("user.buyer.queue"),
    USER_SELLER("user.seller.queue"),
    USER_NOTICE("user.notice.queue"),
    USER_TEMP_PASSWORD("user.temp.password.queue"),
    RETRY("retry.queue");

    private final String queue;
}