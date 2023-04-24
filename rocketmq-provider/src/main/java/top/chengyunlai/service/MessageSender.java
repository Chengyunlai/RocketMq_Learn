package top.chengyunlai.service;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import top.chengyunlai.bean.UserDTO;

import java.util.UUID;

/**
 * @ClassName
 * @Description
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Service
public class MessageSender {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    @Qualifier("dataUpdateRocketMQTemplate")
    private RocketMQTemplate dataUpdateRocketMQTemplate;

    // 同步消息
    public void sendMessage() {
        // 同步发送消息
        // 第一个参数是指定消息的 topic ，第二个参数是消息的内容（类型为 Object ，只不过这里演示方便我们选择发送普通文本作为消息体）。
        rocketMQTemplate.convertAndSend("test-sender", "你好，这是同步消息");
    }

    public void sendMessage2() {
        // 同步发送消息
        // 第一个参数是指定消息的 topic ，第二个参数是消息的内容（类型为 Object ，只不过这里演示方便我们选择发送普通文本作为消息体）。
        rocketMQTemplate.convertAndSend("test-sender2", "你好，这是重传消息");
    }

    // 异步消息
    public void asyncSend() {
        // 异步发送消息
        rocketMQTemplate.asyncSend("test-sender", "你好，这是异步消息", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步消息发送成功");
                System.out.println(sendResult);
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("消息发送失败，异常：" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // 发送单向消息
    public void sendOneway() {
        System.out.println(System.currentTimeMillis());
        // 发送单向消息
        rocketMQTemplate.sendOneWay("test-sender", "你好，这是单向消息");
        System.out.println("发送单向消息成功");
        System.out.println(System.currentTimeMillis());
    }

    // 使用Tag，两个topic都是test-tags，两个的tag不同，一个为software；另外一个是hardware
    //  SpringBoot 整合 RocketMQ 后会变得特别简单，只需要在发送 / 接收消息的 topic 后添加 ":tags" 即可。
    public void sendTagsMessage() {
        // 发送tags为"software"和"hardware"的消息
        rocketMQTemplate.convertAndSend("test-tags:software", "tag是software");
        rocketMQTemplate.convertAndSend("test-tags:hardware", "tag是hardware");
    }

    public void sendDto() {
        // 构造发送一个DTO模型
        UserDTO dto = new UserDTO();
        dto.setUserId(1);
        dto.setUserName("chengyunlai");
        dto.setSexName("male");
        rocketMQTemplate.convertAndSend("test-dto", dto);
    }

    // 使用 RocketMQTemplate 发送延迟消息的方式很简单，
    // 只需要在 syncSend 或者 asyncSend 方法的原有参数后面追加 2 个参数即可
    // ，分别是消息发送的最大等待时间，以及延迟的时间档位。
    // RocketMQ 中内置了 18 个延迟时间档位，
    // 分别是 1s / 5s / 10s / 30s / 1m / 2m / 3m / 4m / 5m / 6m / 7m / 8m / 9m / 10m / 20m / 30m / 1h / 2h ，
    // 这些档位都是内置的，通常我们只能选择，没办法自行指定。
    public void sendDelayMessage() {
        // 同步发送延迟消息
        Message<String> message = MessageBuilder.withPayload("delay message").build();
        rocketMQTemplate.syncSend("test-delay", message, 3000, 2); // 此处用第2档 5秒
        System.out.println("同步延迟消息发送成功！时间：" + System.currentTimeMillis());

        // 异步发送延迟消息
        rocketMQTemplate.asyncSend("test-delay", message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步延迟消息发送成功！时间：" + System.currentTimeMillis());
                System.out.println(sendResult.toString());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("异步延迟消息发送失败！");
                e.printStackTrace();
            }
        }, 3000, 3); // 此处用第3档 10秒
    }

    /**
     * @Description: sendMessageInTransaction ，注意这个方法不能直接放消息正文，而是我们自己使用 MessageBuilder 来构建消息实体。
     *
     * RocketMQ 给我们提供了一个回调接口机制：RocketMQLocalTransactionListener ，这个事务监听器具备执行本地事务和查询本地事务状态的能力。
     * 执行本地事务代码（也就是执行 Service 中标注了 @Transactional 注解的方法）正是在这里面回调。
     *
     * @Param: []
     * @return: void
     * @Author: chengyunlai
     * @Date: 2023/4/24
     */
    // 事务型消息操作
    public void sendTransactionalMessage(){
        // 第1步：发送事务消息，为了能识别出某个事务消息对应的本地事务，此处生成一个uuid设置到消息头上
        String txId = UUID.randomUUID().toString();

        // sendMessageInTransaction 可以搭配 implements RocketMQLocalTransactionListener
        rocketMQTemplate.sendMessageInTransaction("test-transactional",
                MessageBuilder.withPayload("事务消息").setHeaderIfAbsent("txId", txId).build(), txId);
        // 第三个参数可以传入任意参数，此处来传入对应的事务标识uuid（根据业务需求来，通常不传）
    }

    // 多个主题的事务消息场景
    public void sendUpdateTransactionalMessage() {
        // 换一个Template发送
        String txId = UUID.randomUUID().toString();
        dataUpdateRocketMQTemplate.sendMessageInTransaction("test-transactionalupdate",
                MessageBuilder.withPayload("update事务消息").setHeaderIfAbsent("txId", txId).build(), null);
    }

    // 顺序消费
    public void send10Message() {
        for (int i = 0; i < 10; i++) {
            // 无序的
            // rocketMQTemplate.syncSend("test-ordered", "message" + i);

            // 生产者这里的改动，还需要再改动消费者的地方：在 @RocketMQMessageListener 注解上有一个属性 consumeMode ，它可以指定是并发消费还是顺序消费。我们把这个属性显式指定为 ORDERLY 顺序消费。
            // 有序的：这个方法需要我们在方法参数列表的最后传入一个 hash 的值（相当于 Map 中的 key ），
            // RocketMQTemplate 在底层实际发送消息时会先计算传入的 hash key 应该对应 topic 中的哪个队列，计算完成后将消息放入算好的那个队列中。
            rocketMQTemplate.syncSendOrderly("test-ordered", "ordered-message" + i, "ordered");
        }
    }
}