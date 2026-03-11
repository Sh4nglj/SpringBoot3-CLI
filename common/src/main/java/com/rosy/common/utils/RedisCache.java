package com.rosy.common.utils;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        Assert.notNull(redisTemplate, "redisTemplate must not be null");
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(value, "value must not be null");
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(value, "value must not be null");
        Assert.notNull(timeout, "timeout must not be null");
        Assert.notNull(timeUnit, "timeUnit must not be null");
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        Assert.hasText(key, "key must not be empty");
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(unit, "unit must not be null");
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        Assert.hasText(key, "key must not be empty");
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        Assert.hasText(key, "key must not be empty");
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheObject(final String key) {
        Assert.hasText(key, "key must not be empty");
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return (T) operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     * @return true=删除成功；false=删除失败
     */
    public boolean deleteObject(final String key) {
        Assert.hasText(key, "key must not be empty");
        Boolean result = redisTemplate.delete(key);
        return result != null && result;
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return true=删除成功；false=删除失败
     */
    public boolean deleteObject(final Collection<String> collection) {
        Assert.notEmpty(collection, "collection must not be empty");
        Long result = redisTemplate.delete(collection);
        return result != null && result > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(dataList, "dataList must not be null");
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 缓存List数据并设置过期时间
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @param timeout  超时时间
     * @param unit     时间单位
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList, final long timeout, final TimeUnit unit) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(dataList, "dataList must not be null");
        Assert.notNull(unit, "unit must not be null");
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        if (count != null && count > 0) {
            expire(key, timeout, unit);
        }
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getCacheList(final String key) {
        Assert.hasText(key, "key must not be empty");
        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
        return range != null ? (List<T>) range : Collections.emptyList();
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    @SuppressWarnings("unchecked")
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        Assert.hasText(key, "key must not be empty");
        Assert.notNull(dataSet, "dataSet must not be null");
        BoundSetOperations<String, T> setOperation = (BoundSetOperations<String, T>) redisTemplate.boundSetOps(key);
        for (T item : dataSet) {
            setOperation.add(item);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getCacheSet(final String key) {
        Assert.hasText(key, "key must not be empty");
        Set<Object> members = redisTemplate.opsForSet().members(key);
        return members != null ? (Set<T>) members : Collections.emptySet();
    }

    /**
     * 缓存Map
     *
     * @param key     缓存键值
     * @param dataMap 缓存的数据
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        Assert.hasText(key, "key must not be empty");
        if (dataMap != null && !dataMap.isEmpty()) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getCacheMap(final String key) {
        Assert.hasText(key, "key must not be empty");
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries == null || entries.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put((String) entry.getKey(), (T) entry.getValue());
        }
        return result;
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        Assert.hasText(key, "key must not be empty");
        Assert.hasText(hKey, "hKey must not be empty");
        Assert.notNull(value, "value must not be null");
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheMapValue(final String key, final String hKey) {
        Assert.hasText(key, "key must not be empty");
        Assert.hasText(hKey, "hKey must not be empty");
        HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
        return (T) opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<?> hKeys) {
        Assert.hasText(key, "key must not be empty");
        Assert.notEmpty(hKeys, "hKeys must not be empty");
        List<Object> result = redisTemplate.opsForHash().multiGet(key, (Collection) hKeys);
        return result != null ? (List<T>) result : Collections.emptyList();
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        Assert.hasText(key, "key must not be empty");
        Assert.hasText(hKey, "hKey must not be empty");
        Long result = redisTemplate.opsForHash().delete(key, hKey);
        return result != null && result > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        Assert.hasText(pattern, "pattern must not be empty");
        Set<String> result = redisTemplate.keys(pattern);
        return result != null ? result : Collections.emptySet();
    }
}
