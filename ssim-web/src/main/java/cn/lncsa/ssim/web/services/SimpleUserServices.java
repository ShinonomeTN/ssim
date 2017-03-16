package cn.lncsa.ssim.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by cattenlinger on 2017/3/9.
 */
@Service
public class SimpleUserServices {

    private Properties userProperties;
    private Map<String, Long> tokenPool;

    public SimpleUserServices() {
        tokenPool = new HashMap<>();
    }

    @Autowired
    public void setUserProperties(Properties userProperties) {
        this.userProperties = userProperties;
    }

    public String generateToken(String username, String password) {
        String passwd = userProperties.getProperty(username);

        if (passwd == null || !password.equals(passwd)) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        tokenPool.put(token, System.currentTimeMillis() + 36000000);
        return token;
    }

    public Date tokenExpireDate(String token) {
        Long ms = tokenPool.get(token);
        if (ms == null) return null;
        return new Date(ms);
    }

    public boolean isTokenExpired(String token) {
        Long tokenPeriod = tokenPool.get(token);
        return tokenPeriod == null || tokenPeriod < System.currentTimeMillis();
    }
}
