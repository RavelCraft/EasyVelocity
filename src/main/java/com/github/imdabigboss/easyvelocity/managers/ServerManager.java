package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.proxy.server.ServerInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerManager {
    private final List<String> servers = new ArrayList<>();
    private final Map<String, ServerInfo> serverInfo = new HashMap<>();
    private final PluginConfig config = EasyVelocity.getConfig("servers");

    public ServerManager() {
        if (this.config.contains("servers")) {
            Object obj = this.config.get("servers");

            if (obj != null) {
                if (obj instanceof List) {
                    this.servers.addAll(this.config.getStringList("servers"));
                }
            }
        }

        for (String server : this.servers) {
            InetSocketAddress socket = new InetSocketAddress(this.config.getString(server + ".address"), this.config.getInt(server + ".port"));
            ServerInfo info = new ServerInfo(server, socket);

            this.serverInfo.put(server, info);

            EasyVelocity.getServer().registerServer(info);
        }
    }

    public boolean addServer(ServerInfo descriptor) {
        if (this.servers.contains(descriptor.getName()) || EasyVelocity.getServer().getServer(descriptor.getName()).isPresent()) {
            return false;
        }

        this.servers.add(descriptor.getName());
        this.serverInfo.put(descriptor.getName(), descriptor);

        this.config.set("servers", this.servers);
        this.config.set(descriptor.getName() + ".address", descriptor.getAddress().getHostString());
        this.config.set(descriptor.getName() + ".port", descriptor.getAddress().getPort());
        this.config.save();

        EasyVelocity.getServer().registerServer(descriptor);

        return true;
    }

    public boolean removeServer(String name) {
        if (!this.servers.remove(name)) {
            return false;
        }
        ServerInfo oldServer = this.serverInfo.remove(name);

        this.config.set("servers", this.servers);
        this.config.set(name, null);
        this.config.save();

        EasyVelocity.getServer().unregisterServer(oldServer);

        return true;
    }

    public List<ServerInfo> getServers() {
        return new ArrayList<>(this.serverInfo.values());
    }
}
