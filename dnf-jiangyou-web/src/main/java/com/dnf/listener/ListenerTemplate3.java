package com.dnf.listener;

import com.dnf.bean.Users;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ListenerTemplate3 implements MessageListener {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        System.out.println(message.toString());
        String msg = new String(message.getBody());
        try {
            Users value = mapper.readValue(msg, new TypeReference<Users>() {
            });
            System.out.println("MQ接受消息--3--"+value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
