package project.forwork.api.mock;

import project.forwork.api.common.service.port.UuidHolder;

public class FakeUuidHolder implements UuidHolder {

    private final String uuid;

    public FakeUuidHolder(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String random() {
        return uuid;
    }
}
