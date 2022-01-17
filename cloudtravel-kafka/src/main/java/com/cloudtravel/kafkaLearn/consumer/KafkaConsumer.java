package com.cloudtravel.kafkaLearn.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 消费者监控类
 * @author Administrator
 */
@Component
public class KafkaConsumer {

    @KafkaListener(id = "testListener" , topics = {"test"} , containerFactory = "kafkaMessageListenerContainer")
    public void onMessageWithTopicTest(ConsumerRecord<? , ?> record) {
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }

    @KafkaListener(topics = {"test5"})
    public void onMessageWithTopicTest5(ConsumerRecord<? , ?> record) {
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }

    @KafkaListener(topics = {"test2"})
    public void onMessageWithTopicTest2(ConsumerRecord<? , ?> record) {
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
}
