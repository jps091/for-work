package project.forwork.api.common.infrastructure;

import org.springframework.stereotype.Component;
import project.forwork.api.common.service.port.ClockHolder;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class SystemClockHolder implements ClockHolder {
    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
    @Override
    public long convertMillisFrom(long minutes) {
        return (minutes * 60 * 1000);
    }

    public Date convertExpiredDateFrom(long millis) {
        long expiredMillis = millis + millis();
        return new Date(expiredMillis);
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
