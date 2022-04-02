package com.cloudtravel.consumer;

import com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class , DubboAutoConfiguration.class})
@EnableScheduling
@ImportResource(locations = {"classpath:spring/*.xml"})
@ComponentScan({"com.cloudtravel.consumer" , "com.cloudtravel.common.redis"})
public class CloudtravelConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelConsumerApplication.class, args);
	}

}
