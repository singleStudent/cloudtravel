package com.cloudtravel.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis连接配置类
 * CachingConfigurerSupport : 缓存使用redis缓存
 * @author Administor
 * @date 2022/02/09
 */
@EnableCaching
@Configuration
public class CloudRedisConfig extends CachingConfigurerSupport{

    @Bean
    public RedisTemplate<String , Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
        //设置连接工厂,工厂中初始化了redis的连接信息
        redisTemplate.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        //制定要序列化的域[包括file/get()/set()] , 以及修饰符范围:ANY: 包括private/public
        om.setVisibility(PropertyAccessor.ALL , JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型,类必须是非final修饰的,final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL ,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //key-value的序列化模式.key采用String类型 , value采用jackson2JsonRedisSerializer执行序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        //设置hash类型的数据的key/value的序列化模式.这里key采用String类型 ,value采用jackson2JsonRedisSerializer
        //不设置的话在针对hash类型的操作时会序列化解析异常
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws Throwable{
        Config config = new Config();
        //集群部署时的效果
//        config.useClusterServers().addNodeAddress();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //看门狗手动指定分布式锁的延期时长
        config.setLockWatchdogTimeout(30);
        //采用哨兵模式实现自动装载
//        return Redisson.create(Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream()));
        return Redisson.create(config);
    }

    /**
     * 初始化redis对hash类型的数据的操作类
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String ,String , Object> hashOperations(RedisTemplate<String , Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 初始化redis对单个数据值的操作类.
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String , Object> valueOperations(RedisTemplate<String , Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 初始化redis对list集合类型的操作类
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String , Object> listOperations(RedisTemplate<String , Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 初始化redis对set集合的操作类
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String , Object> setOperations(RedisTemplate<String , Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 初始化redis对zSet类型数据的操作类
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String , Object> zSetOperations(RedisTemplate<String , Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /*********************************** CacheManager配置 *********************************/


    /**
     * 定义缓存管理器,主要是定义序列化和反序列化支持类
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        RedisSerializationContext.SerializationPair serializationPair = RedisSerializationContext.
                SerializationPair.fromSerializer(serializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30L))
                .serializeKeysWith(serializationPair);
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new IgnoreExceptionCacheErrorHandler();
    }
}
