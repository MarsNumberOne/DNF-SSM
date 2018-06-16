package com.dnf.listener;

import com.dnf.bean.Users;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * DESCRIPTION:
 * Create on:2018/3/15.
 *
 * @author MACHUNHUI
 */
@Service
@Slf4j
public class ListenerTemplate1 implements MessageListener {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        System.out.println(message.toString());
        String msg = new String(message.getBody());
        try {
            //消息格式化 解决编码问题
            String messageJson = new String(message.getBody(), "UTF-8");
            log.info("监听到消息格式化后为：{}", messageJson);
            Users value = mapper.readValue(msg, new TypeReference<Users>() {
            });
            System.out.println("MQ接受消息--1--"+value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
