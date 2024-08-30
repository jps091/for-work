package project.forwork.api.common.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.forwork.api.common.service.port.RedisUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisStringUtilsImpl implements RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    public void setData(String key, String value, Long ttlSeconds){
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public Long getExpirationTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    public Long incrementData(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    public Long incrementDataInitTimeOut(String key, long timeout) {
        Long incrementedValue = redisTemplate.opsForValue().increment(key);
        Long currentTtl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (currentTtl == -1) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }

        return incrementedValue;
    }

    public String createKeyForm(String prefix, Object userId) {
        return prefix + userId;
    }
}
