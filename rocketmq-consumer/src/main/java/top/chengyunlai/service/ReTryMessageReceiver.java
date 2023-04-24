package top.chengyunlai.service;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName
 * @Description
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Component
@RocketMQMessageListener(topic = "test-sender2", consumerGroup = "myconsumer")
public class ReTryMessageReceiver implements RocketMQListener<MessageExt> {
    // @Override
    // public void onMessage(String message) {
    //     System.out.println(System.currentTimeMillis());
    //     System.out.println("收到消息：" + message);
    //     int i = 1 / 0;
    // }

    // 改造重传，限制一下次数
    @Override
    public void onMessage(MessageExt ext) {
        int reconsumeTimes = ext.getReconsumeTimes();
        if (reconsumeTimes > 3) {
            // 将消息保存到数据库
            System.out.println("消息未被消费，但已经存在数据库中");
            // 直接返回，代表消息已经消费成功
            return;
        }
        System.out.println(System.currentTimeMillis());
        String message = new String(ext.getBody(), StandardCharsets.UTF_8);
        System.out.println("收到消息：" + message);
        int i = 1 / 0;
    }
}
