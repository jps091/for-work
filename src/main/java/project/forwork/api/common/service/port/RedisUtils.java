package project.forwork.api.common.service.port;

public interface RedisUtils {
    void setData(String key, String value, Long expiredTime);

    Long getExpirationTime(String key);

    String getData(String key);

    void deleteData(String key);

    Long incrementData(String key);

    Long incrementDataInitTimeOut(String key, long timeout);

    String createKeyForm(String prefix, Object value);
}
