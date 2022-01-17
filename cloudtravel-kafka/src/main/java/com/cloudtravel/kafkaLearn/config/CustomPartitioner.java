package com.cloudtravel.kafkaLearn.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义分区策略
 * 注意 : 分区策略中返回的分区值要小于或等于Kafka的server.properties中的num.partitions.
 * 若分区值超出num.partitions.消息发送时将因为找不到该分区失败
 */
@Component
public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        if(topic.equals("test5")) {
            return 1;
        }
        if(topic.equals("test2")) {
            return 2;
        }
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
