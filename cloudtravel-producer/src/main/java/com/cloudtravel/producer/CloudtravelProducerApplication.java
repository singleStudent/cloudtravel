package com.cloudtravel.producer;

import cn.dev33.satoken.SaManager;
import com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class , DubboAutoConfiguration.class})
@PropertySources(value = {@PropertySource("classpath:application.properties")})
@ImportResource(locations={"classpath:spring/*.xml"})
@ComponentScan({"com.cloudtravel.producer" , "com.cloudtravel.common.redis"})
public class CloudtravelProducerApplication {

	public static void main(String[] args) {
		 SpringApplication.run(CloudtravelProducerApplication.class, args);
		 System.out.println("Sa-Token 配置信息如下 : " + SaManager.getConfig());
	}

}
