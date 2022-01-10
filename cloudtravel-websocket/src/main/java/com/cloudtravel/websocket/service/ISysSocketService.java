package com.cloudtravel.websocket.service;

/**
 * 系统webSocket服务接口
 * @author Administrator
 */
public interface ISysSocketService {

    public void sendMessage(String userId, String message);
}
