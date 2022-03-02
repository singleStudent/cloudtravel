package com.cloudtravel.shardingsphere;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class ,
		DruidDataSourceAutoConfigure.class})
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@ImportResource(locations={"classpath:META-INF/*.xml"})
@ComponentScan({"com.cloudtravel.shardingsphere.*" , "com.cloudtravel.common.redis"})
@EnableDubbo
public class CloudtravelShardingsphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelShardingsphereApplication.class, args);
	}

}
