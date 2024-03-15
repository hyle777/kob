package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    /**
     * 线程安全的字典：将userId和websocket连接映射起来
     */
    final private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;


    /**
     * 连接会话：实现双向通信
     */
    private Session session;

    // 在不是spring管理的类中注入bean

    public static UserMapper userMapper;

    @Autowired
    void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        System.out.println("connected");
        // 建立连接
        this.session = session;

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        users.put(userId,this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user!=null){
            users.remove(this.user.getId());
        }

    }

    // 接受消息，并相当于路由调用
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parse(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            this.startMatching();
        }else if("stop-matching".equals(event)){
            this.stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 从匹配池选出两个用户，根据用户id找到相应的webSocket连接，并向客户端发送消息
     */
    private void startMatching(){
        System.out.println("start matching");
        matchPool.add(this.user);
        while(matchPool.size() >= 2){
            Game game = new Game(13,14,20);
            game.createGameMap();
            Iterator<User> it = matchPool.iterator();
            User a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);
            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("gamemap",game.getG());
            users.get(a.getId()).sendMessage(JSONObject.toJSONString(respA));
            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("gamemap",game.getG());
            users.get(b.getId()).sendMessage(JSONObject.toJSONString(respB));

        }
    }

    /**
     * 将用户从匹配池删去
     */
    private void stopMatching(){
        System.out.println("stop matching");
        matchPool.remove(this.user);
    }

}