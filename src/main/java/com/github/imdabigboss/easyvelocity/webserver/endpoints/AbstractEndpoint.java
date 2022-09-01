package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;
import com.sun.net.httpserver.HttpExchange;

public abstract class AbstractEndpoint {
    private final EndpointType type;
    private final String path;

    public AbstractEndpoint(EndpointType type, String path) {
        this.type = type;
        this.path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
    }

    protected abstract PageReturn getPageContents(PageRequest request);

    public PageReturn pageQuery(HttpExchange exchange) {
        PageRequest request = new PageRequest(exchange);
        return this.getPageContents(request);
    }

    public boolean canResolve(String path) {
        if (this.type == EndpointType.ALL) {
            return path.startsWith(this.path);
        } else {
            if (path.contains(".")) {
                return path.equals(this.path);
            } else {
                return path.equals(this.path) || path.equals(this.path + "/");
            }
        }
    }

    public EndpointType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }
}

