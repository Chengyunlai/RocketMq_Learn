package top.chengyunlai.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName
 * @Description tag过滤，只收test-tags主题下的software信息
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Component
@RocketMQMessageListener(topic = "test-tags", consumerGroup = "tagsconsumer", selectorExpression = "software")
public class SoftwareMessageReceiver implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("收到software消息：" + message);
    }
}
