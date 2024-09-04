package project.forwork.api.mock;

import project.forwork.api.common.service.port.UuidHolder;

public class TestUuidHolder implements UuidHolder {

    private final String uuid;

    public TestUuidHolder(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String random() {
        return uuid;
    }
}
