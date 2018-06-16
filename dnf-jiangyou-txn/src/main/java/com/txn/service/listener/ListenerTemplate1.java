package com.txn.service.listener;

import com.alibaba.dubbo.common.json.JSON;
import com.dnf.bean.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * DESCRIPTION:
 * Create on:2018/3/15.
 *
 * @author MACHUNHUI
 */
@Service
@Slf4j
public class ListenerTemplate1 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            //消息格式化 解决编码问题
            String messageJson = new String(message.getBody(), "UTF-8");
            log.info("监听到消息格式化后为：{}", messageJson);
            Users parse = JSON.parse(messageJson, Users.class);
            System.out.println("测试用JSON.parse转格式："+ parse);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
