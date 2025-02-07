package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("")
    public String Hello(){
        return "Hello World";
    }

    /**
     * 使用myBatics接口进行CRUD
     */
    @GetMapping("/user/all")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable int userId){
        //return userMapper.selectById(userId);

        //使用条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);

        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/user/getUsers")
    public List<User> getUsers(){
        //范围查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.gt("id",0).lt("id",3);
        queryWrapper.ge("id",1).le("id",2);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/user/add/{userId}/{userName}/{password}")
    public String addUser(@PathVariable int userId,
                          @PathVariable String userName,
                          @PathVariable String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userId,userName,encodedPassword,"",1500);
        userMapper.insert(user);
        return "Add user successfully";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUserById(@PathVariable int userId){
        userMapper.deleteById(userId);
        return "Delete User successfully";
    }
    /**/


}
