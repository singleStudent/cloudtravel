package com.cloudtravel.consumer.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 使用spring boot框架传统的tomcat容器管理bean时 , 需要声明ServerEndpointExporter类
 * ServerEndpointExporter : spring的socket连接管理容器, 既然是连接就需要容器类进行管理 .
 * 该类就起到声明ServerEndpoint注解和管理socket连接欸的作用
 * @author Administrator
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
