package project.forwork.api.common.infrastructure;

import org.springframework.stereotype.Component;
import project.forwork.api.common.service.port.ClockHolder;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class SystemClockHolder implements ClockHolder {
    public long millis() {
        return Clock.systemUTC().millis();
    }
    public long convertSecondsFrom(long minutes) {
        return (minutes * 60);
    }

    public Date convertExpiredDateFrom(long millis) {
        long expiredMillis = millis * 1000 + millis();
        return new Date(expiredMillis);
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDate nowDate(){
        return LocalDate.now();
    }
}
