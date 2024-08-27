package project.forwork.api.common.config.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    public static final String SHORT_STRING_CACHE = "shortStringCache";
    public static final String SHORT_JSON_CACHE = "shortJsonCache";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration jsonCacheConfig = createRedisCacheConfiguration(new Jackson2JsonRedisSerializer<>(Object.class));
        RedisCacheConfiguration stringCacheConfig = createRedisCacheConfiguration(new StringRedisSerializer());

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        setCacheConfigurations(cacheConfigurations, SHORT_STRING_CACHE, jsonCacheConfig, 5);
        setCacheConfigurations(cacheConfigurations, SHORT_JSON_CACHE, stringCacheConfig, 5);


        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private RedisCacheConfiguration createRedisCacheConfiguration(RedisSerializer<?> redisSerializer){
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
    }

    private void setCacheConfigurations(
            Map<String, RedisCacheConfiguration> cacheConfiguration,
            String cacheKey,
            RedisCacheConfiguration redisCacheConfiguration,
            long ttlInMinutes
    ){
        cacheConfiguration.put(cacheKey, redisCacheConfiguration.entryTtl(Duration.ofMinutes(ttlInMinutes)));
    }


}
