package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;

public class RobotsEndpoint extends AbstractEndpoint {
    private final PageReturn page;

    public RobotsEndpoint() {
        super(EndpointType.SIMPLE, "/robots.txt");

        this.page = new PageReturn("User-agent: *\nDisallow: /\n");
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        return this.page;
    }
}

