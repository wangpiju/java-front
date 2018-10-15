package com.hs3.home.utils;

import com.hs3.web.utils.SpringContext;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtils {
    private static final Logger logger = Logger.getLogger(RedisUtils.class);
    private static JedisPool jedisPool;
    private static ThreadLocal<Jedis> jedis = new ThreadLocal<Jedis>();

    private static boolean getConfig() {
        try {
            if (jedisPool == null) {
                jedisPool = (JedisPool) SpringContext.getBean("jedisPool");
                if (jedisPool == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private static Jedis getJedis(boolean create) {
        try {
            Jedis j = (Jedis) jedis.get();
            if ((j == null) && (create)) {
                j = jedisPool.getResource();
                jedis.set(j);
            }
            return j;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static void close() {
        try {
            Jedis j = getJedis(false);
            if (j != null) {
                j.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            jedis.remove();
        }
    }

    public static boolean del(String key) {
        if (!getConfig()) {
            return false;
        }
        try {
            getJedis(true).del(key);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            close();
        }
    }

    public static String get(String key) {
        if (!getConfig()) {
            return null;
        }
        try {
            return getJedis(true).get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            close();
        }
        return null;
    }

    public static void set(String key, String value) {
        set(key, value, 0);
    }

    public static void set(String key, String value, int seconds) {
        if (!getConfig()) {
            return;
        }
        try {
            getJedis(true).set(key, value);
            if (seconds > 0) {
                getJedis(true).expire(key, seconds);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            close();
        }
    }

    public static String hget(String key, String field) {
        if (!getConfig()) {
            return null;
        }
        try {
            return getJedis(true).hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            close();
        }
        return null;
    }

    public static boolean hset(String key, String field, String value) {
        if (!getConfig()) {
            return false;
        }
        try {
            getJedis(true).hset(key, field, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            close();
        }
    }
}
