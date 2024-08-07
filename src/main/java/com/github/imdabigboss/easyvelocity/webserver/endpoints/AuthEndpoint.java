package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;
import com.github.imdabigboss.easyvelocity.webserver.WebSession;

public class AuthEndpoint extends AbstractEndpoint {
    public AuthEndpoint() {
        super(EndpointType.SIMPLE, "/auth");
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        PageReturn pageReturn = new PageReturn("Redirecting...", 302, true);
        WebSession session = request.getSession();

        if (!session.getData().containsKey("loggedIn")) {
            String username = request.getQueries().get("username");
            String password = request.getQueries().get("password");
            if (username != null && password != null) {
                if (username.equals("admin") && password.equals("admin")) {
                    session.getData().put("loggedIn", true);
                    pageReturn.setRedirect("/");
                } else { // Wrong username or password
                    pageReturn.setRedirect("/?error=1");
                }
            } else { // No username or password
                pageReturn.setRedirect("/?error=2");
            }
        } else { // Already logged in
            pageReturn.setRedirect("/");
        }

        return pageReturn;
    }
}
