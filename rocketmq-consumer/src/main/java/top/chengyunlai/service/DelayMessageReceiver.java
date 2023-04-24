package top.chengyunlai.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName
 * @Description 消费延迟消息
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Component
@RocketMQMessageListener(topic = "test-delay", consumerGroup = "delayconsumer")
public class DelayMessageReceiver implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("接收到消息：" + message);
        System.out.println(System.currentTimeMillis());
    }
}
