package project.forwork.api.common.producer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Exchange {
    AUTH("auth.direct.x"),
    USER("user.topic.x"),
    RETRY("retry.fanout.x");

    private final String exchange;
}

