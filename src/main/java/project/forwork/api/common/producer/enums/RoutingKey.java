package project.forwork.api.common.producer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoutingKey {
    AUTH_VERIFY("auth.verify"),
    USER_BUYER("user.buyer"),
    USER_SELLER_SELLING("user.seller.selling"),
    USER_SELLER_RESULT("user.seller.result"),
    USER_NOTICE("user.notice"),
    USER_ADMIN_INQUIRY("user.admin.inquiry"),
    USER_PASSWORD("user.password");

    private final String key;
}
