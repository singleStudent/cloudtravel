package com.cloudtravel.shardingsphere;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class ,
		DruidDataSourceAutoConfigure.class})
@EnableDubbo
@ComponentScan({"com.cloudtravel.shardingsphere" , "com.cloudtravel.common.redis"})
public class CloudtravelShardingsphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelShardingsphereApplication.class, args);
	}

}
