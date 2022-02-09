package com.cloudtravel.common.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;


/**
 * 缓存读取失败时,不抛出异常 . 保证业务继续向下执行
 */
@Slf4j
public class IgnoreExceptionCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error("RedisError :HandleCacheGetError , key={} , cache={} , message={}" , key , cache , exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.error("RedisError :HandleCachePutError , key={}, value={}, cache={} , message={}"
                , key , value , cache , exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error("RedisError :handleCacheEvictError , key={}, cache={} , message={}"
                , key  , cache , exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("RedisError :HandleCacheClearError , cache={} , message={}"
                , cache , exception.getMessage());
    }
}
