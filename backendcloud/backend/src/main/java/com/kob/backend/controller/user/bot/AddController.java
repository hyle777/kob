package com.kob.backend.controller.user.bot;

import com.kob.backend.service.impl.user.bot.AddServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddController {

    @Autowired
    AddServiceImpl addService;

    @PostMapping("/api/user/bot/add/")
    Map<String,String> add(@RequestParam Map<String,String> data){
        return addService.add(data);
    }

}
