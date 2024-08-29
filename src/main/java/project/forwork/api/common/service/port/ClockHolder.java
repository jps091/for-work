package project.forwork.api.common.service.port;

import java.time.LocalDateTime;
import java.util.Date;

public interface ClockHolder {
    long millis();
    long convertSecondsFrom(long minutes);
    LocalDateTime now();
    Date convertExpiredDateFrom(long millis);
}
