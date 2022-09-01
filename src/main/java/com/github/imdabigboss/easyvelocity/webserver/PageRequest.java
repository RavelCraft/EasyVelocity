package com.github.imdabigboss.easyvelocity.webserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.InetSocketAddress;

public class PageRequest {
    private final String path;
    private final RequestMethod requestMethod;
    private final InetSocketAddress ipAddress;
    private final Headers headers;

    public PageRequest(HttpExchange exchange) {
        this.path = exchange.getRequestURI().getPath();
        this.requestMethod = RequestMethod.fromString(exchange.getRequestMethod());
        this.ipAddress = exchange.getRemoteAddress();
        this.headers = exchange.getRequestHeaders();
    }

    public String getPath() {
        return this.path;
    }

    public RequestMethod getRequestMethod() {
        return this.requestMethod;
    }

    public InetSocketAddress getIpAddress() {
        return this.ipAddress;
    }

    public Headers getHeaders() {
        return this.headers;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "path='" + this.path + "'" +
                ", requestMethod='" + this.requestMethod.name() + "'" +
                ", ipAddress='" + this.ipAddress + "'" +
                ", headers={...}" +
                '}';
    }
}

