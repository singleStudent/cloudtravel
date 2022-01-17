package com.cloudtravel.kafkaLearn;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableDubbo
@EnableKafka
public class CloudtravelKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelKafkaApplication.class, args);
	}

}
