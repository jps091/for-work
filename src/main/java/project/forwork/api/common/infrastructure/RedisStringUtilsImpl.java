package project.forwork.api.common.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.forwork.api.common.service.port.RedisUtils;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisStringUtilsImpl implements RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setDataWithTimeout(String key, String value, Long ttlSeconds){
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getExpirationTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    @Override
    public Long incrementData(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long incrementDataInitTimeOut(String key, long timeout) {
        Long incrementedValue = redisTemplate.opsForValue().increment(key);
        Long currentTtl = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if (currentTtl == -1) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }

        return incrementedValue;
    }

    @Override
    public String createKeyForm(String prefix, Object value) {
        return prefix + value;
    }
}
