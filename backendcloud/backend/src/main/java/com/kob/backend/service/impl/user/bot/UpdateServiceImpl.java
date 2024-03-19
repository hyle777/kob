package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Map<String,String> map = new HashMap<>();
        if(title == null || title.length() == 0){
            map.put("error_message", "标题不能为空");
            return  map;
        }
        if(title.length() > 100){
            map.put("error_message","标题不能超过100个字符");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户很懒，什么也没留下";
        }
        if(description.length() > 300){
            map.put("error_message","描述不能超过300个字符");
            return map;
        }
        if(content == null || content.length() == 0) {
            map.put("error_message", "代码长度不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("error_message", "代码长度不能超过10000个字符");
            return map;
        }
        int id = Integer.parseInt(data.get("id"));
        Bot bot = botMapper.selectById(id);
        if(bot == null){
            map.put("error_message","Bot不存在或已被删除");
            return map;
        }
        if(bot.getUserId() != user.getId()){
            map.put("error_message","没有权限修改这个bot");
            return map;
        }
        Bot bot1 = new Bot(bot.getId(),bot.getUserId(),title,description,content,bot.getCreateTime(),new Date());
        botMapper.updateById(bot1);
        map.put("error_message","update success");
        return map;
    }
}
