package top.chengyunlai.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import top.chengyunlai.bean.UserDTO;

/**
 * @ClassName
 * @Description要么继续用 String 接收，之后手动执行 json 转对象的方式拿到原始 DTO 对象；
 *
 * 要么直接在泛型类型上标注 DTO 对象。
 *
 * 下面的代码中我们使用第二种方式，直接以 UserDTO 的形式接收。
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Component
@RocketMQMessageListener(topic = "test-dto", consumerGroup = "dtoconsumer")
public class UserDtoMessageReceiver implements RocketMQListener<UserDTO> {

    @Override
    public void onMessage(UserDTO message) {
        System.out.println("接收到DTO消息");
        System.out.println(message.toString());
    }
}