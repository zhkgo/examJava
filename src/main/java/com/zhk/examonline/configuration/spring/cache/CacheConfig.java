package com.zhk.examonline.configuration.spring.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

/*  @Autowired
    private CacheProperties cacheProperties;*/

    /**
     * redis序列化修改为ObjectMapper
     *
     * @return RedisCacheConfiguration
     */
/*    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }*/


    /**
     * spring boot redis默认序列化方式
     *
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * spring redis 默认生成key方式,包含::号
     *
     * @param prefix
     * @param key
     * @return
     */
    public String simpleKeyGenerator(String prefix, String key) {
        return CacheKeyPrefix.simple().compute(prefix) + key;
    }
}
