package com.txn.service.impl;

import com.dnf.bean.Users;
import com.dnf.dao.UsersDao;
import com.txn.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DESCRIPTION:
 * Create on:2018/3/7.
 *
 * @author MACHUNHUI
 */
//注入的时候定义名字，@Resource，@Autowired 引入的时候必须使用对应的命名。
@Service("uService")
@Slf4j
public class UsersServiceImpl implements UsersService {
    @Resource
    private UsersDao usersDao;

    @Resource(name = "amqpTemplate1")
    private AmqpTemplate amqpTemplate1;

    @Resource(name = "amqpTemplate3")
    private AmqpTemplate amqpTemplate3;

    @Override
    public Users getUsersByUsername(String username) {
        System.out.println("--------业务层--------- usersByUsername："+username+"-----------------");
        Users usersByUsername = null;
        try {
            usersByUsername= usersDao.getUsersByUsername(username);
            System.out.println("--------业务层---------"+usersByUsername+"-----------------");
        }catch (Exception e){
            System.out.println("--------业务层---------异常"+e.toString()+"-----------------");
        }
        return usersByUsername;
    }
    @Override
    public void sendMessage(){
        try {
            send1();
            send2();
            send3();
        }catch (Exception e){
            System.out.println("---业务层异常--"+e);
        }
    }
    private void send1(){
        Users users = new Users();
        users.setUsername("First马春晖");
        users.setPassword("11111");
        users.setId(1111);
        System.out.println("---业务层SendMessage--1--"+users);
        amqpTemplate1.convertAndSend("key1",users);
        System.out.println("---业务层SendMessage发送完成 ！--1--");
    }
    private void send2(){
        Users users = new Users();
        users.setUsername("Second马春晖");
        users.setPassword("22222");
        users.setId(2222);
        System.out.println("---业务层SendMessage--2--"+users);
        amqpTemplate1.convertAndSend("key2",users);
        System.out.println("---业务层SendMessage发送完成 ！--2--");
    }
    private void send3(){
        Users users = new Users();
        users.setUsername("Third马春晖");
        users.setPassword("33333");
        users.setId(3333);
        System.out.println("---业务层SendMessage--3--"+users);
        amqpTemplate3.convertAndSend("key3",users);
        System.out.println("---业务层SendMessage发送完成 ！--3--");
    }
}
