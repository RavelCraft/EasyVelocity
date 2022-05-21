package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.*;

public class PermissionsManager {
    private final PluginConfig config = EasyVelocity.getConfig("permissions");
    private final Map<UUID, List<String>> permissions = new HashMap<>();

    public PermissionsManager() {
        for (String uuidString : this.config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);

            this.permissions.put(uuid, this.config.getStringList(uuidString));
        }
    }

    public boolean hasPermission(CommandSource source, String permission) {
        if (source instanceof ConsoleCommandSource) {
            return true;
        }

        Player player = (Player) source;
        UUID playerUUID = player.getUniqueId();

        UUID originalPlayerUUID = EasyVelocity.getNickManager().getOriginalPlayer(player.getUniqueId());
        if (originalPlayerUUID != null) {
            playerUUID = originalPlayerUUID;
        }

        if (this.permissions.containsKey(playerUUID)) {
            for (String perm : this.permissions.get(playerUUID)) {
                if (perm.equals(permission)) {
                    return true;
                } else if (perm.equals("*")) {
                    return true;
                } else {
                    if (perm.length() < permission.length() && !perm.endsWith("*")) {
                        return false;
                    }

                    for (int i = 0; i < perm.length(); i++) {
                        if (perm.charAt(i) == '*') {
                            return true;
                        } else if (perm.charAt(i) != permission.charAt(i)) {
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void addPermission(CommandSource source, String permission) {
        if (source instanceof ConsoleCommandSource) {
            return;
        }

        Player player = (Player) source;
        if (!this.permissions.containsKey(player.getUniqueId())) {
            this.permissions.put(player.getUniqueId(), new ArrayList<>());
        }

        List<String> playerPerms = this.permissions.get(player.getUniqueId());
        if (!playerPerms.contains(permission)) {
            playerPerms.add(permission);

            this.config.set(player.getUniqueId().toString(), playerPerms);
            this.config.save();
        }
    }

    public void removePermission(CommandSource source, String permission) {
        if (source instanceof ConsoleCommandSource) {
            return;
        }

        Player player = (Player) source;
        if (!this.permissions.containsKey(player.getUniqueId())) {
            return;
        }

        List<String> playerPerms = this.permissions.get(player.getUniqueId());
        if (playerPerms.contains(permission)) {
            playerPerms.remove(permission);

            this.config.set(player.getUniqueId().toString(), playerPerms);
            this.config.save();
        }
    }
}
