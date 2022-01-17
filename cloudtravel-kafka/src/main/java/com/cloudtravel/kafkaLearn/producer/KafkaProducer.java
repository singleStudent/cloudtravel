package com.cloudtravel.kafkaLearn.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产者demo
 * @author Administrator
 */
@RestController
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String , Object> kafkaTemplate;

    /**
     * 带回调方法的消息发送1 . 回调方法1->Java8的流式回调
     * @param message
     */
    @GetMapping("/kafkaProducer/send1/{topic}/{message}")
    public void sendMessage1(@PathVariable("topic")String topic , @PathVariable("message")String message){
        kafkaTemplate.send(topic  , message).addCallback(success -> {
            System.out.println("消息发送成功 : " + success.getProducerRecord().key()  + "-" + success.getProducerRecord().value() +
            "===> offset = " + success.getRecordMetadata().offset() + "-topic = " + success.getRecordMetadata().topic()
             +" partition = " + success.getRecordMetadata().partition());
        } , failure -> {
            System.out.println("消息发送失败 : " + failure.getMessage());
        });
    }



    /**
     * 带回调方法的消息发送1 . 回调方法2
     * 写法2可以理解为写法1的补充写法 .
     * @param message
     */
    @GetMapping("/kafkaProducer/send2/{message}")
    public void sendMessage2(@PathVariable("message")String message){
        kafkaTemplate.send("test"  , "testKey" , message).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("消息发送失败 : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("消息发送成功 : " + result.getProducerRecord().key()  + "-" + result.getProducerRecord().value() +
                        "===> offset = " + result.getRecordMetadata().offset() + "-topic = " + result.getRecordMetadata().topic()
                        +" partition = " + result.getRecordMetadata().partition());
            }
        });
    }

    /**
     * 带回调方法的消息发送1 . 回调方法1->Java8的流式回调
     * @param message
     */
    @GetMapping("/kafkaProducer/send1WithTx/{topic}/{message}")
    public void sendMessage1WithTx(@PathVariable("topic")String topic , @PathVariable("message")String message){
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send(topic  , message).addCallback(success -> {
                System.out.println("消息发送成功-事务 : " + success.getProducerRecord().key()  + "-" + success.getProducerRecord().value() +
                        "===> offset = " + success.getRecordMetadata().offset() + "-topic = " + success.getRecordMetadata().topic()
                        +" partition = " + success.getRecordMetadata().partition());
            } , failure -> {
                System.out.println("消息发送失败-事务 : " + failure.getMessage());
            });
            throw new RuntimeException("test");
        });

    }
}
