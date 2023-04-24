package top.chengyunlai.listener;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import top.chengyunlai.service.DataService;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName
 * @Description RocketMQ 给我们提供了一个回调接口机制：RocketMQLocalTransactionListener ，这个事务监听器具备执行本地事务和查询本地事务状态的能力。
 *
 * 缺点：无法区分业务场景的痛点
 * 最头疼的问题是 RocketMQLocalTransactionListener 这里，因为一个项目中只能注册一个
 * RocketMQLocalTransactionListener （不信邪的小伙伴可以自行测试一下，如果注册多个，会在启动过程中抛出异常），只有一个 Listener 就没办法从 bean 的范围区分了，那我们只好在方法体内部来区分。
 *
 * 解决方案：
 * 1. 它给出的方案是用不同的 RocketMQTemplate 来区分不同的业务场景：一个分布式事务场景用一个单独的 RocketMQTemplate 来发送事务消息。这看上去貌似挺离谱的
 *
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@RocketMQTransactionListener
public class TransactionalStatusChecker implements RocketMQLocalTransactionListener {

    // 事务
    @Autowired
    private DataService dataService;

    // 对应第 3 步的执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // 注意刚拿到的是 byte[] ，需要转成 String
        String payload = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        System.out.println(payload);
        System.out.println(arg);

        RocketMQLocalTransactionState state = RocketMQLocalTransactionState.COMMIT;

        try {
            // dataService.saveData();
            dataService.saveData(payload, msg.getHeaders().get("txId", String.class));
        } catch (Exception e) {
            state = RocketMQLocalTransactionState.UNKNOWN;
        }
        return state;
    }



    // checkLocalTransaction 方法则是对应第 6 步的本地事务是否执行完毕
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return dataService.contains(msg.getHeaders().get("txId", String.class))
                ? RocketMQLocalTransactionState.COMMIT
                : RocketMQLocalTransactionState.ROLLBACK;
    }
}