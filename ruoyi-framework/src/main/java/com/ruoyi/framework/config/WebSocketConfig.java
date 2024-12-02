package com.ruoyi.framework.config;

import com.ruoyi.framework.channel.PodLogChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter (){
        ServerEndpointExporter exporter = new ServerEndpointExporter();
        // 注册 WebSocket 端点
        exporter.setAnnotatedEndpointClasses(PodLogChannel.class);
        // ...
        return exporter;
    }
}
