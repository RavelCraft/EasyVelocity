package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.*;

public class WhitelistManager {
    private List<String> whitelist = null;
    private List<String> enabledServers = null;
    private final Map<String, List<String>> perServerWhitelist = new HashMap<>();
    private final PluginConfig config = EasyVelocity.getConfig("whitelist");

    public WhitelistManager() {
        if (this.config.contains("whitelist")) {
            Object obj = this.config.get("whitelist");

            if (obj != null) {
                if (obj instanceof List) {
                    this.whitelist = this.config.getStringList("whitelist");
                }
            }
        }
        if (this.whitelist == null) {
            this.whitelist = new ArrayList<>();
        }

        if (this.config.contains("enabled")) {
            Object obj = this.config.get("enabled");

            if (obj != null) {
                if (obj instanceof List) {
                    this.enabledServers = this.config.getStringList("enabled");
                }
            }
        }
        if (this.enabledServers == null) {
            this.enabledServers = new ArrayList<>();
        }

        for (RegisteredServer server : EasyVelocity.getServer().getAllServers()) {
            String serverName = server.getServerInfo().getName();

            if (this.config.contains("server." + serverName)) {
                Object obj = this.config.get("server." + serverName);

                if (obj != null) {
                    if (obj instanceof List) {
                        this.perServerWhitelist.put(serverName, this.config.getStringList("server." + serverName));
                    }
                }
            }

            if (!this.perServerWhitelist.containsKey(serverName)) {
                this.perServerWhitelist.put(serverName, new ArrayList<>());
            }
        }
    }

    public boolean isWhitelisted(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        return this.whitelist.contains(uuid.toString());
    }

    public boolean isWhitelisted(UUID uuid, String server) {
        if (uuid == null || server == null) {
            return false;
        }

        if (this.enabledServers.contains(server)) {
            return this.perServerWhitelist.get(server).contains(uuid.toString());
        }
        return true;
    }

    public boolean addPlayer(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        if (this.whitelist.contains(uuid.toString())) {
            return false;
        } else {
            this.whitelist.add(uuid.toString());

            this.config.set("whitelist", this.whitelist);
            this.config.save();

            return true;
        }
    }

    public boolean addPlayer(UUID uuid, String server) {
        if (uuid == null || server == null) {
            return false;
        }

        if (this.perServerWhitelist.containsKey(server)) {
            List<String> serverWhitelist = this.perServerWhitelist.get(server);

            if (serverWhitelist.contains(uuid.toString())) {
                return false;
            } else {
                serverWhitelist.add(uuid.toString());

                this.config.set("server." + server, serverWhitelist);
                this.config.save();

                return true;
            }
        }

        return false;
    }

    public boolean removePlayer(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        boolean wasThere = this.whitelist.remove(uuid.toString());
        if (wasThere) {
            this.config.set("whitelist", this.whitelist);
            this.config.save();
        }
        return wasThere;
    }

    public boolean removePlayer(UUID uuid, String server) {
        if (uuid == null || server == null) {
            return false;
        }

        if (this.perServerWhitelist.containsKey(server)) {
            List<String> serverWhitelist = this.perServerWhitelist.get(server);

            boolean wasThere = serverWhitelist.remove(uuid.toString());
            if (wasThere) {
                this.config.set("server." + server, serverWhitelist);
                this.config.save();
            }
            return wasThere;
        }
        return false;
    }

    public void setEnabled(String server, boolean enabled) {
        if (this.enabledServers.contains(server)) {
            if (!enabled) {
                this.enabledServers.remove(server);
            }
        } else {
            if (enabled) {
                this.enabledServers.add(server);
            }
        }
        this.config.set("enabled", this.enabledServers);
        this.config.save();
    }
}
