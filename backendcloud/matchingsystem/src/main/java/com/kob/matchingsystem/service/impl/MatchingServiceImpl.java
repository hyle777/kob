package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add User:" + " " + userId + " " + rating);
        matchingPool.addPlayer(userId, rating, botId);
        return "add User success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove User:" + " " + userId);
        matchingPool.removePlayer(userId);
        return "remove user success";
    }
}
