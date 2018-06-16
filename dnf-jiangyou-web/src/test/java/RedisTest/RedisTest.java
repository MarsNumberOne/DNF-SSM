package RedisTest;

import com.common.redis.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * DESCRIPTION:
 * Create on:2018/3/20.
 *
 * @author MACHUNHUI
 */
//加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml"})
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;

    @Resource(name = "caseTemplate")
    private RedisTemplate caseTemplate;

    @Test
    public void test(){
        ValueOperations valueOperations = caseTemplate.opsForValue();//操作字符串
        HashOperations hashOperations = caseTemplate.opsForHash();//操作hash
        ListOperations listOperations = caseTemplate.opsForList();//操作list
        SetOperations setOperations = caseTemplate.opsForSet();//操作set
        ZSetOperations zSetOperations = caseTemplate.opsForZSet();//操作有序set
        //查询mch的value下标为0-2的字符串
        String mch = valueOperations.get("mch", 0, 2);
        String mch1 = (String) valueOperations.get("mch");
        System.out.println(mch+"--"+mch1);
    }
    @Test
    public void setCaseTest(){
        try {
            boolean set = redisUtil.set("dnf", "恍惚");
            caseTemplate.opsForValue().set("date","神威");
            System.out.println(set);
        }catch (Exception e){
            System.out.println("Redis 插入失败：【"+e.toString()+"】");
        }
    }
    @Test
    public void getValueTest(){
        try {
            Object dnf = redisUtil.get("dnf");
            Object mch = redisUtil.get("mch");
            Object date = caseTemplate.opsForValue().get("date");
            System.out.println(dnf);
            System.out.println(mch);
            System.out.println(date);
        }catch (Exception e){
            System.out.println("Redis 获取失败：【"+e.toString()+"】");
        }
    }
    @Test
    public void remoValueTest(){
        try {
            redisUtil.remove("dnf");
            redisUtil.remove("date");
        }catch (Exception e){
            System.out.println("Redis 删除失败：【"+e.toString()+"】");
        }
    }
}
