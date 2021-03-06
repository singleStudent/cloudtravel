package com.cloudtravel.websocket;

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
@ImportResource(locations={"classpath:META-INF/*.xml"})
@ComponentScan("com.cloudtravel.websocket.*.*")
public class CloudtravelWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudtravelWebsocketApplication.class, args);
	}


}
