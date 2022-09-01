package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.utils.FileUtils;
import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.MimeType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;

public class JarEndpoint extends AbstractEndpoint {
    private final String resourcePath;

    public JarEndpoint(EndpointType type, String path, String resourcePath) {
        super(type, path);
        this.resourcePath = resourcePath;
    }

    private String getPath(PageRequest request) {
        if (this.getType() == EndpointType.ALL) {
            String path = request.getPath().substring(this.getPath().length());
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            return this.resourcePath + path;
        } else {
            return this.resourcePath;
        }
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        String path = this.getPath(request);
        String extension = path.substring(path.lastIndexOf('.') + 1);

        try {
            return new PageReturn(FileUtils.resourceToBytes(path), MimeType.getMimeType(extension).getMimeType());
        } catch (NullPointerException e) {
            return new PageReturn("Page not found", 404);
        }
    }
}
