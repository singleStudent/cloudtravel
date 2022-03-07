package com.cloudtravel.kafkaLearn.consumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消费者监控类
 * @author Administrator
 */
@Component
public class KafkaConsumer {

    @KafkaListener(id = "testListener" , topics = {"test"})
    public void onMessageWithTopicTest(List<ConsumerRecord<? , ?>> records) {
        records.stream().forEach(record->{
                    System.out.println("test："+ record.topic()+"-"+record.partition()+"-"+record.value());
                }
        );
    }

    @KafkaListener(topics = {"test5"})
    public void onMessageWithTopicTest5(List<ConsumerRecord<? , ?>> records)  {
        records.stream().forEach(record->{
                    System.out.println("test5："+ record.topic()+"-"+record.partition()+"-"+record.value());
                }
        );
    }

    @KafkaListener(topics = {"test2"})
    public void onMessageWithTopicTest2(List<ConsumerRecord<? , ?>> records) {
        records.stream().forEach(record->{
                    System.out.println("test2："+ record.topic()+"-"+record.partition()+"-"+record.value());
                }
        );
    }
}
