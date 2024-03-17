package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Snake {
    private Integer id;
    private Integer sx;
    private Integer sy;

    /**
     * 存储玩家的操作序列
     */
    private List<Integer> steps;

    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    /**
     * @return 返回蛇所占的单元格坐标列表
     */
    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();
        int[] dx = {0,1,0,-1},dy = {-1,0,1,0};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for(int d : steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if(!check_tail_increasing(++step)){
                steps.remove(0);
            }
        }
        return res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }
}
