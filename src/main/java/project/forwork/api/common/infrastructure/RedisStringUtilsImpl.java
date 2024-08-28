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

    public void setData(String key, String value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
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
}
