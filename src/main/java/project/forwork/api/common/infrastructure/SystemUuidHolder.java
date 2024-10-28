package project.forwork.api.common.infrastructure;

import org.springframework.stereotype.Component;
import project.forwork.api.common.service.port.UuidHolder;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString().substring(0, 7) + "@";
    }
}
