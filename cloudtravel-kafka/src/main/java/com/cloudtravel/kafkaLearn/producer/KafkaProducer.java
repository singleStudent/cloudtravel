package com.cloudtravel.kafkaLearn.producer;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String , Object> kafkaTemplate;

    @GetMapping("/kafkaProducer/send/{message}")
    public void sendMessage(@PathVariable("message")String message){
        kafkaTemplate.send("test"  , "testKey" , message).addCallback(success -> {
            System.out.println("消息发送成功 : " + success.getProducerRecord().key()  + "-" + success.getProducerRecord().value() +
            "===> offset = " + success.getRecordMetadata().offset() + "-topic = " + success.getRecordMetadata().topic()
             +" partition = " + success.getRecordMetadata().partition());
        } , failure -> {
            System.out.println("消息发送失败 : " + failure.getMessage());
        });
    }
}
