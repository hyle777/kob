package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{

    private Integer rows;
    private Integer cols;
    private Integer inner_walls_count;
    private int[][] g;
    final static int[] dx = {-1, 0, 1, 0}, dy ={0,1,0,-1};

    private Snake snakeA, snakeB;

    ReentrantLock lock = new ReentrantLock();
    private String status = "playing";// playing->finshed
    private String loser = "";// all：平局 A:A输 B:B输

    private String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    /**
     * 多个进程对nextStepA,nextStepB进行读写
     */
    private Integer nextStepA = null,nextStepB = null;






    public Game(Integer rows, Integer cols, Integer inner_walls_count,
                Integer idA,
                Bot botA,
                Integer idB,
                Bot botB){
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        snakeA = new Snake(idA, botIdA, botCodeA, 1, rows - 2, new ArrayList<>());
        snakeB = new Snake(idB, botIdB, botCodeB, cols - 2, 1, new ArrayList<>());

    }
    public int[][] getG(){
        return g;
    }
    public Snake getSnakeA(){
        return snakeA;
    }

    public Snake getSnakeB(){
        return snakeB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
        this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public void createGameMap(){
        for (int i = 0; i < 1000; i++) {
            if (this.create_walls()) {
                break;
            }
        }
    }
    private Boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int nx = sx + dx[i],
                    ny = sy + dy[i];
            if (g[nx][ny] == 0 && this.check_connectivity(nx, ny, tx, ty)) {
                g[sx][sy] = 0;
                return true;
            }
        }
        g[sx][sy] = 0;
        return false;
    }
    Boolean create_walls() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                g[r][c] = 0;
            }
        }
        //四周加上墙
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = 1;
            g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(rows);
                int c = random.nextInt(cols);
                if (g[r][c] == 1|| g[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }
                if (
                        (r == this.rows - 2 && c == 1) ||
                                (r == 1 && c == this.cols - 2)
                ) {
                    continue;
                }
                //轴对称
                //g[r][c] = g[c][r] = true;
                //中心对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2,1,1,this.cols-2);
    }


    private String getInput(Snake snake) {  // 将当前的局面信息，编码成字符串
        Snake me, you;
        if (snakeA.getId().equals(snake.getId())) {
            me = snakeA;
            you = snakeB;
        } else {
            me = snakeB;
            you = snakeA;
        }
        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }

    private void sendBotCode(Snake snake) {
        if (snake.getBotId().equals(-1)) return;  // 亲自出马，不需要执行代码
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", snake.getId().toString());
        data.add("bot_code", snake.getBotCode());
        data.add("input", getInput(snake));
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }


    /**
     * 等待两名玩家的输入，5s则视为超时输入
     *
     * @return 是否获取了输入
     */
    boolean nextStep(){
        try{
            //蛇每秒走5个格子，每次玩家输入一次操作，移动一格
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendBotCode(snakeA);
        sendBotCode(snakeB);
        for(int i = 0; i < 50; i++) {
            try{
                Thread.sleep(100);
                lock.lock();
                try{
                    if (nextStepA != null && nextStepB != null){
                        snakeA.getSteps().add(nextStepA);
                        snakeB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //5s则视为超时
        return false;
    }


    /**
     * 检测蛇头CellsA[n-1]是否有效
     * @param cellsA A蛇的坐标列表
     * @param cellsB B蛇的坐标列表
     * @return
     */
    boolean check_valid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if(g[cell.y][cell.x] == 1){ //撞墙了
            return false;
        }
        for(int i = 0; i < n-1; i++){
            Cell cell1 = cellsA.get(i);
            if(cell.x == cell1.x && cell.y == cell1.y){
                return false;
            }
        }
        for(int i = 0; i < n; i++){
            Cell cell1 = cellsB.get(i);
            if(cell.x == cell1.x && cell.y == cell1.y){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断用户下一步操作nextStep是否合法
     *
     * 不合法 修改loser status，调用setResult
     * 合法 sendMove
     */
    void judge(){
        List<Cell> cellsA = snakeA.getCells();
        List<Cell> cellsB = snakeB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if(!validA || !validB){
            status = "finished";
        }
        if(!validA && !validB){
            loser = "all";
        } else if(!validA){
            loser = "A";
        } else if(!validB){
            loser = "B";
        }

    }

    void sendAllMessage(String msg){
        WebSocketServer.users.get(snakeA.getId()).sendMessage(msg);
        WebSocketServer.users.get(snakeB.getId()).sendMessage(msg);
    }
    void sendMove(){
        //想直接传走完一步后两条蛇的坐标数组好像行不通
        //        JSONObject resp = new JSONObject();
        //        List<Cell> cellsA = snakeA.getCells();
        //        List<Cell> cellsB = snakeB.getCells();
        //        resp.put("event", "move");
        //        resp.put("cellsA",cellsA);
        //        resp.put("cellsB",cellsB);
        //        sendAllMessage(resp.toJSONString());
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        }finally{
            lock.unlock();
        }


    }

    void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }
    @Override
    public void run() {
        super.run();
        // 100个空格 3步长1 100*3 在1000步内能走完
        for (int i = 0;i < 1000; i++) {
            // 能接收到两边客户端的输入
            if (nextStep()) {
                judge();
                if ("playing".equals(this.status)) {
                    sendMove();
                } else{
                    sendResult();
                    break;
                }
            } else {
                lock.lock();
                try {
                    status = "finished";
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    }
                    else if (nextStepA == null) {
                        loser = "A";
                    } else if (nextStepB == null) {
                        loser = "B";
                    }
                } finally {
                lock.unlock();

                }
                sendResult();
                break;
            }

        }
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++ ) {
            for (int j = 0; j < cols; j ++ ) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void updateUserRating(Snake snake, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(snake.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);

    }

    private void saveToDatabase() {

        Integer ratingA = WebSocketServer.userMapper.selectById(snakeA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(snakeB.getId()).getRating();
        if ("A".equals(loser)) {
            ratingA -= 10;
            ratingB += 5;
        } else if ("B".equals(loser)) {
            ratingA += 5;
            ratingB -= 10;
        }
        updateUserRating(snakeA, ratingA);
        updateUserRating(snakeB, ratingB);
        Record record = new Record(
                null,
                snakeA.getId(),
                snakeA.getSx(),
                snakeA.getSy(),
                snakeB.getId(),
                snakeB.getSx(),
                snakeB.getSy(),
                snakeA.getStepsString(),
                snakeB.getStepsString(),
                getMapString(),
                loser,
                new Date(),
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }
}
