package com.common.redis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <b>redis锁实现</b><br/> 处理分布式高并发的方式 1.数据库乐观锁 2.基于Redis的分布式锁 3.zk的分布式锁
 * <b>Create on:</b>2018/2/27 16:04<br/>
 *
 * @author : liud<br/>
 */
@Component
@Slf4j
public class DistributedLockHandler {
    /**
     * 单个业务持有锁的时间10s,防止死锁
     */
    private final static long LOCK_EXPIRE = 10 * 1000L;
    /**
     * 默认30ms尝试一次
     */
    private final static long LOCK_TRY_INTERVAL = 30L;
    /**
     * 默认获取不到就结束
     */
    private final static long LOCK_TRY_TIMEOUT = 0 * 1000L;

    @Resource(name="strRedisTemplate")
    private RedisTemplate template;

    /**
     * 操作redis获取全局锁
     *                  如果key存在返回false；如果key不存在，就存入reidis返回true,由此来处理高并发
     * @param lock           锁的名称
     * @param timeout        获取的超时时间
     * @param tryInterval    多少ms尝试一次
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    private boolean  getLock(Lock lock,long timeout,long tryInterval,long lockExpireTime){
        try{
            if(StringUtils.isEmpty(lock.getName()) || StringUtils.isEmpty(lock.getValue())){
                return false;
            }
            long startTime = System.currentTimeMillis();
            while (true){
                //如果key存在返回false；如果key不存在，就存入reidis返回true,由此来处理高并发
                if(template.opsForValue().setIfAbsent(lock.getName(),lock.getValue())){
                    template.expire(lock.getName(),lockExpireTime,TimeUnit.MILLISECONDS);
                    log.info(Thread.currentThread().getName() + "lock.getName() : get lock");
                    return true;
                } else {
                    log.info(Thread.currentThread().getName() + "lock.getName() : ----> locking is exist!!!");
                }
                if(System.currentTimeMillis() - startTime > timeout){
                    return false;
                }
                Thread.sleep(tryInterval);
            }
        } catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock    锁的名称
     * @param timeout 获取超时时间 单位ms
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout) {
        return getLock(lock, timeout, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock        锁的名称
     * @param timeout     获取锁的超时时间
     * @param tryInterval 多少毫秒尝试获取一次
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval) {
        return getLock(lock, timeout, tryInterval, LOCK_EXPIRE);
    }


    /**
     * 尝试获取全局锁
     *
     * @param lock           锁的名称
     * @param timeout        获取锁的超时时间
     * @param tryInterval    多少毫秒尝试获取一次
     * @param lockExpireTime 锁的过期
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval, long lockExpireTime) {
        return getLock(lock, timeout, tryInterval, lockExpireTime);
    }

    /**
     * 释放锁
     */
    public void releaseLock(Lock lock) {
        if (!StringUtils.isEmpty(lock.getName())) {
            log.info(Thread.currentThread().getName() + " : del lock");
            template.delete(lock.getName());
        }
    }
}
