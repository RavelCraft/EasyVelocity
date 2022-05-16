package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaintenanceManager {
    private List<UUID> maintenanceBypass = new ArrayList<>();
    private boolean maintenanceMode = false;
    private final PluginConfig config = EasyVelocity.getConfig();

    public MaintenanceManager() {
        if (config.contains("bypassMaintenance")) {
            Object obj = this.config.get("bypassMaintenance");

            if (obj != null) {
                if (obj instanceof List) {
                    for (String uuidString : this.config.getStringList("bypassMaintenance")) {
                        try {
                            UUID uuid = UUID.fromString(uuidString);
                            this.maintenanceBypass.add(uuid);
                        } catch (Exception e) {
                            EasyVelocity.getLogger().error("Failed to parse UUID from config: " + uuidString);
                        }
                    }
                }
            }
        }

        if (config.contains("maintenance")) {
           this.maintenanceMode = config.getBoolean("maintenance");
        }
    }

    public boolean canPlayerBypass(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        return this.maintenanceBypass.contains(uuid);
    }

    public void setPlayerBypass(UUID uuid, boolean bypass) {
        if (uuid == null) {
            return;
        }

        if (this.maintenanceBypass.contains(uuid)) {
            if (!bypass) {
                this.maintenanceBypass.remove(uuid);
            }
        } else {
            if (bypass) {
                this.maintenanceBypass.add(uuid);
            }
        }

        List<String> uuids = new ArrayList<>();
        for (UUID u : this.maintenanceBypass) {
            uuids.add(u.toString());
        }
        this.config.set("bypassMaintenance", uuids);
        this.config.save();
    }

    public void setMaintenance(boolean maintenance) {
        this.config.set("maintenance", maintenance);
        this.config.save();
        this.maintenanceMode = maintenance;

        if (maintenance) {
            for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                if (!canPlayerBypass(player.getUniqueId())) {
                    player.disconnect(getMaintenanceMessage(player.getUsername()));
                }
            }
        }
    }

    public boolean isMaintenance() {
        return this.maintenanceMode;
    }

    public static Component getMaintenanceMessage(String username) {
        return Component.text(ChatColor.RED + "We are very sorry " + username + ", but the server is currently under maintenance. Please try again later.");
    }
}
