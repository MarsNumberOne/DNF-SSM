package com.txn.service.listener;

import com.alibaba.fastjson.JSON;
import com.dnf.bean.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * DESCRIPTION:
 * Create on:2018/3/16.
 *
 * @author MACHUNHUI
 */
@Service
@Slf4j
public class ListenerTemplate2 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String messageJson = new String(message.getBody(), "UTF-8");
            Users users = JSON.parseObject(messageJson, Users.class);
            System.out.println("测试用JSON.parseObject转格式: "+users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
