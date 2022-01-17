package com.cloudtravel.kafkaLearn.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"test"})
    public void onMessageWithTopicTest(ConsumerRecord<? , ?> record) {
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
}
