package project.forwork.api.common.config.rabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExchangeType {
    AUTH("auth.direct.exchange"),
    USER("user.topic.exchange"),
    RETRY("retry.fanout.exchange");

    private final String exchange;
}

