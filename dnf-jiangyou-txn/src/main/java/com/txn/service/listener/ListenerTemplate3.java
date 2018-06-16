package com.txn.service.listener;

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
 * Create on:2018/3/16.
 *
 * @author MACHUNHUI
 */
@Service
@Slf4j
public class ListenerTemplate3 implements MessageListener {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        try {
            String messageJson = new String(message.getBody(), "UTF-8");
            Users value = mapper.readValue(messageJson, new TypeReference<Users>() {
            });
            System.out.println("测试用ObjectMapper.readValue转格式："+value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
