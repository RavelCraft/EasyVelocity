package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.webserver.*;

import java.util.UUID;

public class GameEndpoint extends AbstractEndpoint {
    public GameEndpoint() {
        super(EndpointType.SIMPLE, "/game");
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        if (!request.getQueries().containsKey("username") || !request.getQueries().containsKey("password") || !request.getQueries().containsKey("name") || !request.getQueries().containsKey("uuid")) {
            return new PageReturn("Invalid request", 400, true);
        }

        String username = request.getQueries().get("username");
        String password = request.getQueries().get("password");
        String name = request.getQueries().get("name");
        String uuid = request.getQueries().get("uuid");

        boolean out;
        try {
            out = EasyVelocity.getCrackedPlayerManager().crackedPlayerLogin(name, UUID.fromString(uuid), username, password);
        } catch (IllegalArgumentException e) {
            return new PageReturn("Invalid UUID", 400, true);
        }

        if (out) {
            return new PageReturn("OK", 200, true);
        } else {
            return new PageReturn("Invalid username or password", 400, true);
        }
    }
}
