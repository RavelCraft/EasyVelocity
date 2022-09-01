package com.github.imdabigboss.easyvelocity.webserver;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.utils.FileUtils;
import com.github.imdabigboss.easyvelocity.utils.Lock;
import com.github.imdabigboss.easyvelocity.webserver.endpoints.AbstractEndpoint;
import com.github.imdabigboss.easyvelocity.webserver.endpoints.JarEndpoint;
import com.github.imdabigboss.easyvelocity.webserver.endpoints.RHTMLEndpoint;
import com.github.imdabigboss.easyvelocity.webserver.endpoints.RobotsEndpoint;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WebServer implements HttpHandler {
    private static WebServer instance;

    private final HttpServer httpServer;

    private final List<AbstractEndpoint> endpoints = new ArrayList<>();

    private final int maxServerThreads = 10;
    private final Lock threadLock = new Lock();
    private int threadCount = 0;
    private final Queue<HttpExchange> exchangeQueue = new LinkedList<>();

    public static void create() {
        if (!EasyVelocity.getConfig().contains("webserver.port")) {
            EasyVelocity.getLogger().error("WebServer port not found in config.yml. Aborting.");
            return;
        }

        InetSocketAddress inetSocketAddress = new InetSocketAddress(EasyVelocity.getConfig().getInt("webserver.port"));
        HttpServer httpServer;

        try {
            httpServer = HttpServer.create(inetSocketAddress, 0);
            httpServer.setExecutor(null);
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Error creating web server, aborting", e);
            return;
        }

        instance = new WebServer(httpServer);
        EasyVelocity.getLogger().info("Web server listening on port " + inetSocketAddress.getPort());

        instance.start();
    }

    public static void reload() {
        if (instance == null) {
            return;
        }

        instance.reloadInternal();
    }

    private WebServer(HttpServer httpServer) {
        this.httpServer = httpServer;
        this.httpServer.createContext("/", this);

        this.reloadInternal();
    }

    public void start() {
        this.httpServer.start();
    }

    public void reloadInternal() {
        endpoints.clear();

        endpoints.add(new RobotsEndpoint());
        endpoints.add(new JarEndpoint(EndpointType.SIMPLE, "/favicon.ico", "web/favicon.ico"));
        endpoints.add(new JarEndpoint(EndpointType.SIMPLE, "/icon.png", "web/icon.png"));

        endpoints.add(new JarEndpoint(EndpointType.ALL, "/css", "web/css"));
        endpoints.add(new JarEndpoint(EndpointType.ALL, "/js", "web/js"));
        endpoints.add(new JarEndpoint(EndpointType.ALL, "/img", "web/img"));

        try {
            Path markdownRoot = Paths.get("plugins/" + PluginInfo.NAME + "/www");

            List<Path> files = Files.walk(markdownRoot)
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.toString().endsWith(".rhtml"))
                    .toList();

            for (Path file : files) {
                endpoints.add(RHTMLEndpoint.create(file));
            }
        } catch (IOException | ParserConfigurationException | SAXException | IllegalStateException e) {
            EasyVelocity.getLogger().error("Error loading web markdown files", e);
        }
    }

    private void threadHandle(HttpExchange exchange) {
        AbstractEndpoint bestEndpoint = null;
        for (AbstractEndpoint endpoint : this.endpoints) {
            if (endpoint.canResolve(exchange.getRequestURI().getPath())) {
                bestEndpoint = endpoint;
                break;
            }
        }

        PageReturn pageReturn;
        if (bestEndpoint != null) {
            pageReturn = bestEndpoint.pageQuery(exchange);
        } else {
            pageReturn = new PageReturn("Page not found", 404);
        }

        byte[] data;
        if (pageReturn.getStatusCode() == 200) {
            data = pageReturn.getData();
        } else {
            data = FileUtils.resourceToString("web/error.html")
                    .replace("${error}", pageReturn.getStatusCode() + " - " + new String(pageReturn.getData(), StandardCharsets.UTF_8))
                    .replace("${header}", FileUtils.resourceToString("web/content/header.html"))
                    .getBytes();
        }

        exchange.getResponseHeaders().add("Server", "RavelCraft");
        exchange.getResponseHeaders().add("Date", new Date().toString());
        exchange.getResponseHeaders().add("Content-Type", pageReturn.getMime());

        try {
            exchange.sendResponseHeaders(pageReturn.getStatusCode(), data.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(data);
            outputStream.flush();
            exchange.close();
        } catch (IOException ignored) {
        }

        threadLock.lock();
        if (!exchangeQueue.isEmpty()) { // If there are more requests to handle, handle them
            HttpExchange nextExchange = exchangeQueue.poll();
            threadLock.unlock();

            threadHandle(nextExchange);
        } else {
            threadLock.unlock();
        }
    }

    @Override
    public void handle(HttpExchange exchange) {
        threadLock.lock();
        if (threadCount < maxServerThreads) { // Create a worker thread to handle the request
            threadCount++;
            threadLock.unlock();

            new Thread(() -> {
                threadHandle(exchange);

                threadLock.lock();
                threadCount--;
                threadLock.unlock();
            }).start();
        } else { // Queue the request
            exchangeQueue.add(exchange);
            threadLock.unlock();
        }
    }
}
