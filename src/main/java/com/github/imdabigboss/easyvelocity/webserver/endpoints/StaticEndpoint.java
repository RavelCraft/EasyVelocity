package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.webserver.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticEndpoint extends AbstractEndpoint {
    private final boolean cache;
    private final Map<String, PageReturn> pageCache = new HashMap<>();

    public StaticEndpoint(EndpointType type, String path, boolean cache) {
        super(type, path);
        this.cache = cache;

        Path pageRoot = WebServer.WEB_DIR.resolve(path.substring(1)); //Substring to remove the leading /

        if (Files.isDirectory(pageRoot)) {
            if (type == EndpointType.SIMPLE) {
                throw new IllegalStateException("StaticEndpoint pointing to a directory cannot be of type SIMPLE");
            }
        } else {
            if (type == EndpointType.ALL) {
                throw new IllegalStateException("StaticEndpoint pointing to a file cannot be of type ALL");
            }
        }

        if (cache) {
            if (type == EndpointType.SIMPLE) {
                String extension = path.substring(path.lastIndexOf('.') + 1);
                MimeType mime = MimeType.getMimeType(extension);
                if (mime == null) {
                    mime = MimeType.PLAIN;
                }

                try {
                    byte[] page = Files.readAllBytes(pageRoot);
                    PageReturn pageReturn = new PageReturn(page, mime.getMimeType());

                    this.pageCache.put(path, pageReturn);
                } catch (IOException e) {
                    EasyVelocity.getLogger().error("Failed to read static file: " + pageRoot + " path: " + path, e);
                }
            } else {
                try {
                    List<Path> files = Files.walk(pageRoot)
                            .filter(file -> !Files.isDirectory(file))
                            .toList();

                    for (Path file : files) {
                        String extension = file.toString().substring(file.toString().lastIndexOf('.') + 1);
                        MimeType mime = MimeType.getMimeType(extension);
                        if (mime == null) {
                            mime = MimeType.PLAIN;
                        }

                        String pathString = path + file.toString().substring(pageRoot.toString().length());
                        byte[] page = Files.readAllBytes(file);
                        PageReturn pageReturn = new PageReturn(page, mime.getMimeType());

                        this.pageCache.put(pathString, pageReturn);
                    }
                } catch (IOException e) {
                    EasyVelocity.getLogger().error("Failed to read static directory: " + pageRoot + " path: " + path, e);
                }
            }
        }
    }

    public StaticEndpoint(EndpointType type, String path) {
        this(type, path, true);
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        if (this.cache) {
            PageReturn pageReturn = this.pageCache.get(request.getPath());
            if (pageReturn == null) {
                return new PageReturn("Page not found", 404);
            }

            return pageReturn;
        } else {
            Path pageRoot = WebServer.WEB_DIR.resolve(request.getPath().substring(1)); //Substring to remove the leading /
            if (!pageRoot.toAbsolutePath().startsWith(WebServer.WEB_DIR)) {
                return new PageReturn("Forbidden", 403);
            }

            if (Files.isDirectory(pageRoot)) {
                return new PageReturn("Page not found", 404);
            } else {
                String extension = request.getPath().substring(request.getPath().lastIndexOf('.') + 1);
                MimeType mime = MimeType.getMimeType(extension);
                if (mime == null) {
                    mime = MimeType.PLAIN;
                }

                try {
                    byte[] page = Files.readAllBytes(pageRoot);
                    return new PageReturn(page, mime.getMimeType());
                } catch (IOException e) {
                    EasyVelocity.getLogger().error("Failed to read static file: " + pageRoot + " path: " + request.getPath(), e);
                    return new PageReturn("Page not found", 404);
                }
            }
        }
    }
}
