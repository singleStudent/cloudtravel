package com.cloudtravel.websocket.socket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * javax.websocket 使用实例
 * @author Administrator
 */
@Slf4j
@ServerEndpoint("/websocket/webSocketByTomcat/{userId}")
@Component
public class WebSocketService {

    /** 在线人数 */
    private static AtomicInteger socketOnlineCount = new AtomicInteger();

    /** 当前在线的用户缓存 */
    private static ConcurrentHashMap<String , WebSocketService> connectClients = new ConcurrentHashMap<>(1000);

    private static final int MAX_CONNECTION = 1000;

    private String userId;

    private Session session;

    @OnOpen
    public void onOpen(Session session , @PathParam("userId") String userId) {
        this.userId = userId;
        this.session = session;
        if(StringUtils.isBlank(userId)) {
            return;
        }
        if(connectClients.contains(userId)) {
            connectClients.remove(userId);
            connectClients.put(userId , this);
        }else {
            if(connectClients.size() > MAX_CONNECTION) {
                log.error("Socket Connection has over limit " + MAX_CONNECTION);
                return;
            }
            connectClients.put(userId , this);
            addSocketOnlineCount();
        }
        try {
            sendMessage("初始化连接成功...");
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(String.format("有新连接加入：{%s}，当前在线人数为：{%s}", session.getId(), connectClients.size()));
    }

    @OnClose
    public void onClose() {
        System.out.println(String.format("onMessage : value = {%s}" , userId));
        connectClients.remove(this.userId);
        reduceSocketOnlineCount();
    }

    @OnMessage
    public void onMessage(String message) {
        if(StringUtils.isNotBlank(message) && StringUtils.isNotBlank(userId)) {
            System.out.println("userId = " + connectClients.get(userId) + "==>发来消息 = " + message);
        }
    }

    @OnError
    public void onError(Session session , Throwable throwable) {
        System.out.println(String.format("System Error , userId = {%s} , error message = {%s}" ,
                userId , throwable.getMessage()));
    }

    public static synchronized int getSocketOnlineCount() {
        return socketOnlineCount.get();
    }

    public static synchronized void addSocketOnlineCount() {
        WebSocketService.socketOnlineCount.addAndGet(1);
    }

    public static synchronized void reduceSocketOnlineCount() {
        WebSocketService.socketOnlineCount.addAndGet(-1);
    }

    public static synchronized ConcurrentHashMap<String , WebSocketService> getConnectClients() {
        return connectClients;
    }

    public void sendMessage(String message)throws IOException {
        this.session.getBasicRemote().sendText("后端响应 : " + message);
    }

    public static void sendInfoMessage(String message , @PathParam("userId")String userId)throws IOException {
        if(StringUtils.isBlank(userId)) {
            log.error("userId is null");
            return;
        }
        System.out.println(String.format("后端发送消息,message = {%s} , userId = {%s}" , message , userId));
        if(connectClients.containsKey(userId)) {
            connectClients.get(userId).sendMessage(message);
        }else{
            log.error("userId = {%s} 不在线或者不在此服务器中" , userId);
        }
    }

}
