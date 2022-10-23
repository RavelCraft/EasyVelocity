package com.github.imdabigboss.easyvelocity.webserver;

import com.sun.net.httpserver.HttpExchange;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class PageRequest {
    private final String path;
    private final Map<String, String> queries;
    private final RequestMethod requestMethod;
    private final InetSocketAddress ipAddress;
    private final Map<String, String> cookies;
    private final WebSession session;

    public PageRequest(HttpExchange exchange) {
        this.path = exchange.getRequestURI().getPath();
        this.queries = new HashMap<>();
        if (exchange.getRequestURI().getQuery() != null) {
            for (String query : exchange.getRequestURI().getQuery().split("&")) {
                try {
                    String[] split = query.split("=");
                    this.queries.put(split[0], split[1]);
                } catch (Exception ignored) {
                }
            }
        }
        this.requestMethod = RequestMethod.fromString(exchange.getRequestMethod());
        this.ipAddress = exchange.getRemoteAddress();
        this.cookies = new HashMap<>();
        if (exchange.getRequestHeaders().containsKey("Cookie")) {
            for (String cookie : exchange.getRequestHeaders().get("Cookie")) {
                for (String list : cookie.split("; ")) {
                    try {
                        String[] split = list.split("=");
                        this.cookies.put(split[0], split[1]);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        String tmpToken = this.cookies.get("RVSESSID");
        this.session = WebServer.getSession(tmpToken, true);
        if (tmpToken == null || !tmpToken.equals(this.session.getToken())) {
            this.cookies.put("RVSESSID", this.session.getToken());
            exchange.getResponseHeaders().add("Set-Cookie", "RVSESSID=" + this.session.getToken() + "; SameSite=Strict; Max-Age=" + (WebServer.SESSION_EXPIRY_TIME - 10));
        }
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getQueries() {
        return this.queries;
    }

    public RequestMethod getRequestMethod() {
        return this.requestMethod;
    }

    public InetSocketAddress getIpAddress() {
        return this.ipAddress;
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public WebSession getSession() {
        return this.session;
    }
}

