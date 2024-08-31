package project.forwork.api.mock;

import project.forwork.api.common.service.port.RedisUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakeRedisUtils implements RedisUtils {

    private final ConcurrentHashMap<String, String> dataStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> ttlStore = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void setData(String key, String value, Long ttlSeconds) {
        dataStore.put(key, value);
        if (ttlSeconds != null && ttlSeconds > 0) {
            long expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;
            ttlStore.put(key, expiryTime);

            scheduler.schedule(() -> {
                if (System.currentTimeMillis() >= expiryTime) {
                    deleteData(key);
                }
            }, ttlSeconds, TimeUnit.SECONDS);
        }
    }

    public String getData(String key) {
        Long expiryTime = ttlStore.get(key);
        if (expiryTime != null && System.currentTimeMillis() >= expiryTime) {
            deleteData(key);
            return null;
        }
        return dataStore.get(key);
    }

    public Long getExpirationTime(String key) {
        Long expiryTime = ttlStore.get(key);
        if (expiryTime == null) {
            return -1L;
        }
        long timeRemaining = (expiryTime - System.currentTimeMillis()) / 1000;
        return timeRemaining > 0 ? timeRemaining : -1L;
    }

    public void deleteData(String key) {
        dataStore.remove(key);
        ttlStore.remove(key);
    }

    public Long incrementData(String key) {
        String result = dataStore.compute(key, (k, v) -> {
            long newValue = (v == null) ? 1 : Long.parseLong(v) + 1;
            return String.valueOf(newValue);
        });

        return (result != null) ? Long.parseLong(result) : 1L;
    }

    public Long incrementDataInitTimeOut(String key, long timeout) {
        Long incrementedValue = incrementData(key);
        Long currentTtl = getExpirationTime(key);

        if (currentTtl == -1) {
            setData(key, String.valueOf(incrementedValue), timeout);
        }

        return incrementedValue;
    }

    public String createKeyForm(String prefix, Object userId) {
        return prefix + userId;
    }
}
