package com.github.imdabigboss.easyvelocity.webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebSession {
    private final String token;
    private final long updateTime;
    private final Map<String, Object> data;

    public WebSession() {
        this.token = UUID.randomUUID() + "-" + UUID.randomUUID();
        this.updateTime = System.currentTimeMillis();
        this.data = new HashMap<>();
    }

    public String getToken() {
        return this.token;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() + (WebServer.SESSION_EXPIRY_TIME * 1000) < updateTime;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public void removeData(String key) {
        this.data.remove(key);
    }
}
