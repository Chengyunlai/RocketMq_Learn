package top.chengyunlai.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName
 * @Description
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Component
@RocketMQMessageListener(topic = "test-sender", consumerGroup = "myconsumer")
public class MessageReceiver implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("收到消息：" + message);
    }
}
