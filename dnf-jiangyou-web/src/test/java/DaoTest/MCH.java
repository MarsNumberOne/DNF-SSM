package DaoTest;

import com.api.txn.dto.response.SelectlUserByNameResponse;
import com.dnf.bean.Users;
import org.springframework.beans.BeanUtils;
public class MCH {
    public static void main(String[] args) {
        show();
    }
    private static void show(){
        //数据库bean
        Users users = new Users();
        users.setId(12);
        users.setUsername("11");
        users.setPassword("222");
        //返回DTO
        SelectlUserByNameResponse selectlUserByNameResponse = new SelectlUserByNameResponse();
        BeanUtils.copyProperties(users,selectlUserByNameResponse);
        System.out.println(selectlUserByNameResponse);
    }
}
