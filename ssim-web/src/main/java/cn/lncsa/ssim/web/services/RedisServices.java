package cn.lncsa.ssim.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by catten on 3/19/17.
 */
@Service
public class RedisServices {

    public final static String KEY_TERM_LIST = "term-list";
    public final static String KEY_PREFIX_CLASS = "term-";
    public final static String KEY_PREFIX_WEEKS = "term-week-";
    public final static String KEY_PREFIX_TYPES = "term-type-";
    public final static String KEY_TYPES = "type-list";

    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;
    private ValueOperations<String, String> valueOperations;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        listOperations = redisTemplate.opsForList();
        valueOperations = redisTemplate.opsForValue();
    }

    public void setString(String key, String value) {
        valueOperations.set(key, value);
    }

    public String getString(String key) {
        return valueOperations.get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exist(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
    }

    public void putToList(String key, List<String> list) {
        listOperations.leftPushAll(key, list);
    }

    public List<String> getList(String key) {
        return listOperations.range(key, 0, listOperations.size(key));
    }

    public void clearAll() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });
    }

    public Integer getInt(String key) {
        String result = valueOperations.get(key);
        return result == null ? null : Integer.parseInt(result);
    }

    public void setInt(String key, Integer value) {
        valueOperations.set(key, value.toString());
    }
}
