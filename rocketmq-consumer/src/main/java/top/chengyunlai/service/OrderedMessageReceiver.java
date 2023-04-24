package top.chengyunlai.service;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
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
// @RocketMQMessageListener(topic = "test-ordered", consumerGroup = "orderedconsumer")
// 顺序消费
@RocketMQMessageListener(topic = "test-ordered", consumerGroup = "orderedconsumer",consumeMode = ConsumeMode.ORDERLY)
public class OrderedMessageReceiver implements RocketMQListener<String> {

    // 默认是没有顺序的
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}