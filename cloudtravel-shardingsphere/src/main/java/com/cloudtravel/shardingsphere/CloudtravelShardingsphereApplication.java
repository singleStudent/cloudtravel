package com.cloudtravel.shardingsphere;

import com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class ,
		DruidDataSourceAutoConfigure.class , DubboAutoConfiguration.class})
@ImportResource(locations={"classpath:META-INF/*.xml"})
@ComponentScan({"com.cloudtravel.shardingsphere" , "com.cloudtravel.common.redis"})
public class CloudtravelShardingsphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelShardingsphereApplication.class, args);
	}

}
