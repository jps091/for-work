package project.forwork.api.common.producer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueueType {
    VERIFY("verify.q"),
    USER_BUYER("user.buyer.q"),
    USER_SELLER_SELLING("user.seller.selling.q"),
    USER_SELLER_RESULT("user.seller.result.q"),
    USER_NOTICE("user.notice.q"),
    USER_ADMIN_INQUIRY("user.admin.inquiry.q"),
    USER_PASSWORD("user.password.q"),
    RETRY("retry.q");

    private final String queue;
}