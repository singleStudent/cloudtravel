package com.cloudtravel.websocket.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cloudtravel.websocket.service.ISysSocketService;
import com.cloudtravel.websocket.socket.WebSocketService;

import java.io.IOException;

/**
 * @author Administrator
 */
@Service
public class SysSocketServiceImpl implements ISysSocketService {

    @Override
    public void sendMessage(String userId, String message) {
        try {
            WebSocketService.sendInfoMessage(message , userId);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
