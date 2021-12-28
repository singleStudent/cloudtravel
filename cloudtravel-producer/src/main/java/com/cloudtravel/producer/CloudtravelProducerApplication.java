package com.cloudtravel.producer;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDubbo
@EnableScheduling
public class CloudtravelProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelProducerApplication.class, args);
	}

}
