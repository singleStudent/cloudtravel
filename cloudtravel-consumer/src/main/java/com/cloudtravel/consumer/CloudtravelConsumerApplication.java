package com.cloudtravel.consumer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDubbo
@EnableScheduling
@ImportResource(locations = {"classpath:spring/*.xml"})
@ComponentScan({"com.cloudtravel.consumer.*.*" , "com.cloudtravel.common.redis"})
public class CloudtravelConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelConsumerApplication.class, args);
	}

}
