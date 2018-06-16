package Utils;

import com.common.redis.DistributedLockHandler;
import com.common.redis.Lock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

//加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml"})
public class RedisLockTest {
    @Autowired
    private DistributedLockHandler distributedLockHandler;

    @Test
    public void RedisLockTest() throws Exception {
        //同一条请求的唯一字符 作为拼接key的值
        String date = UUID.randomUUID().toString();
        Lock lock = new Lock("Redis_Lock" + "GRANT_" + date , "OK");
        try {
            tryLock(lock);

            //TODO 业务流程
        }catch (Exception e){
            throw e;
        }finally {
            releaseLock(lock);
        }
    }

    /**
     * <b>DESCRIPTION:</b>上锁<br>
     * <b>CREATE ON:</b>2018/5/8<br>
     * <b>AUTHOR:</b>MACHUNHUI
     *
     * @param
     */
    private void tryLock(Lock lock) throws Exception {
        boolean lockFlag = distributedLockHandler.tryLock(lock);
        if (!lockFlag) {
            // 上锁失败
            System.out.println(Thread.currentThread().getName() + "上锁失败");
            throw new Exception();
        }
    }
    /**
     * <b>DESCRIPTION:</b>解锁<br>
     * <b>CREATE ON:</b>2018/5/8<br>
     * <b>AUTHOR:</b>MACHUNHUI
     *
     * @param
     */
    private void releaseLock(Lock lock) {
        if (lock != null) {
            distributedLockHandler.releaseLock(lock);
        }
    }
}
