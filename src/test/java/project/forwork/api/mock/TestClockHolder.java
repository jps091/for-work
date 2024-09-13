package project.forwork.api.mock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import project.forwork.api.common.service.port.ClockHolder;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@Builder
public class TestClockHolder implements ClockHolder {

    private long mills;
    private LocalDateTime localDateTime;

    public TestClockHolder(long mills) {
        this.mills = mills;
    }

    public TestClockHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public long millis() {
        return mills;
    }

    @Override
    public long convertSecondsFrom(long minutes) {
        return minutes;
    }

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }

    @Override
    public Date convertExpiredDateFrom(long millis) {
        long expiredMillis = millis * 1000 + millis();
        return new Date(expiredMillis);
    }
}
