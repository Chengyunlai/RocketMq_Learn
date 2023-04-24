package top.chengyunlai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.chengyunlai.service.MessageSender;

/**
 * @ClassName
 * @Description
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@RestController
public class MessageController {

    @Autowired
    private MessageSender messageSender;

    @GetMapping("/sendMessage")
    public String sendMessage() {
        messageSender.sendMessage();
        return "同步消息发送成功";
    }

    @GetMapping("/reTryMessage")
    public String reTryMessage() {
        messageSender.sendMessage2();
        return "重传消息发送成功";
    }

    @GetMapping("/asyncSend")
    public String asyncSend() {
        messageSender.asyncSend();
        return "异步消息发送成功";
    }

    @GetMapping("/oneWay")
    public String oneWaySend() {
        messageSender.sendOneway();
        return "单向消息发送成功";
    }

    @GetMapping("/tagTOsoftware")
    public String tagSend() {
        messageSender.sendTagsMessage();
        return "发送了test-tags主题的消息，客户端可以选择性的筛选tag";
    }

    @GetMapping("/dtoMessage")
    public String dtoSend() {
        messageSender.sendDto();
        return "发送了一个对象";
    }

    @GetMapping("/delay")
    public String delaySend(){
        messageSender.sendDelayMessage();
        return "延迟消息发送成功";
    }

    @GetMapping("/transaction")
    public String transactionSend(){
        messageSender.sendTransactionalMessage();
        return "事务消息发送成功";
    }

    @GetMapping("/updateTransaction")
    public String updateTransactionSend(){
        messageSender.sendUpdateTransactionalMessage();
        return "update事务消息发送成功";
    }

    @GetMapping("/order")
    public String orderSend(){
        messageSender.send10Message();
        return "顺序消费发送成功";
    }
}
