package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;

public class GameEndpoint extends AbstractEndpoint {
    public GameEndpoint() {
        super(EndpointType.SIMPLE, "/game");
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        if (!request.getQueries().containsKey("username") || !request.getQueries().containsKey("password") || !request.getQueries().containsKey("name")) {
            return new PageReturn("Invalid request", 200, true);
        }

        String username = request.getQueries().get("username");
        String password = request.getQueries().get("password");
        String name = request.getQueries().get("name");

        boolean out;
        try {
            out = EasyVelocity.getCrackedPlayerManager().crackedPlayerLogin(name, username, password);
        } catch (IllegalArgumentException e) {
            return new PageReturn("Invalid UUID", 200, true);
        }

        if (out) {
            EasyVelocity.getLogger().info("Player " + name + " logged in with username " + username);
            return new PageReturn("OK", 200, true);
        } else {
            return new PageReturn("Invalid username or password", 200, true);
        }
    }
}
