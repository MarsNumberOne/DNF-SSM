package DaoTest;

import com.dnf.bean.Users;
import com.dnf.dao.UsersDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * DESCRIPTION:
 * Create on:2018/3/8.
 *
 * @author MACHUNHUI
 */
//加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml"})
public class IUserDaoTest {
    @Autowired
    private UsersDao dao;

    @Test
    public void testSelectUser() throws Exception {
        List<Users> users = dao.selectAllUsers();
        for (Users user : users) {
            System.out.println(user);
        }
    }
    @Test
    public void testSelectUser2() throws Exception {
        List<Users> users = dao.getUsersByUsername2();
        for (Users user : users) {
            System.out.println(user);
        }
    }

}
