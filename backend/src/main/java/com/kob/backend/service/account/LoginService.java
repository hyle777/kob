package com.kob.backend.service.account;

import java.util.Map;

public interface LoginService {
    public Map<String,String> getToken(String username, String password);
}
