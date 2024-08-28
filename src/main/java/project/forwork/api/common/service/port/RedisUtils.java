package project.forwork.api.common.service.port;

import java.util.concurrent.TimeUnit;

public interface RedisUtils {
    void setData(String key, String value, Long expiredTime);

    Long getExpirationTime(String key);

    String getData(String key);

    void deleteData(String key);
}
