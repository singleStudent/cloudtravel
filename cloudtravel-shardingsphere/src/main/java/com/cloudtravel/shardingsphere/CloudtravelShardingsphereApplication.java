package com.cloudtravel.shardingsphere;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class ,
		DruidDataSourceAutoConfigure.class})
public class CloudtravelShardingsphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelShardingsphereApplication.class, args);
	}

}
