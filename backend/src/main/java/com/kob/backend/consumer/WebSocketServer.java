package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
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
    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;

    private Game game = null;


    /**
     * 连接会话：实现双向通信
     */
    private Session session;

    // 在不是spring管理的类中注入bean

    public static UserMapper userMapper;


    public static RecordMapper recordMapper;


    @Autowired
    void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        System.out.println("connected");
        // 建立连接
        this.session = session;

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if(this.user != null){
            users.put(userId,this);
        }else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user!=null){
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }

    }

    /**
     *  接受消息，并相当于路由，调用函数
     *
     * @param message 前端传来的json格式字符串
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            this.startMatching();
        }else if("stop-matching".equals(event)){
            this.stopMatching();
        }else if("move".equals(event)){
            this.move(data.getInteger("direction"));
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
            Iterator<User> it = matchPool.iterator();
            User a = it.next(), b = it.next();
            System.out.println(a.getId());
            System.out.println(b.getId());
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13,14,20,a.getId(),b.getId());
            game.createGameMap();

            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;

            game.start();

            JSONObject respGame = new JSONObject();
            respGame.put("gamemap",game.getG());
            respGame.put("a_id",game.getSnakeA().getId());
            respGame.put("a_sx",game.getSnakeA().getSx());
            respGame.put("a_sy",game.getSnakeA().getSy());
            respGame.put("b_id",game.getSnakeB().getId());
            respGame.put("b_sx",game.getSnakeB().getSx());
            respGame.put("b_sy",game.getSnakeB().getSy());
            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            // 给A发B的信息
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("game",respGame);
            users.get(a.getId()).sendMessage(JSONObject.toJSONString(respA));
            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            //给B发A的信息
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("game",respGame);
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

    private void move(int direction){
        if(game.getSnakeA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }
        else if(game.getSnakeB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

}