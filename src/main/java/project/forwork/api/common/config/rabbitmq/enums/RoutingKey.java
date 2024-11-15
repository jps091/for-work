package project.forwork.api.common.config.rabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoutingKey {
    AUTH_VERIFY("auth.verify"),
    USER_BUYER("user.buyer"),
    USER_SELLER("user.seller"),
    USER_NOTICE("user.notice"),
    USER_TEMP_PASSWORD("user.temp.password");

    private final String key;
}
