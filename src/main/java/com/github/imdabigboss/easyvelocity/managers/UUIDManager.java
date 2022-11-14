package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.github.imdabigboss.easyvelocity.utils.uuid.UUIDFetcher;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UUIDManager {
    private final PluginConfig uuidConfig = EasyVelocity.getConfig("uuid");

    private final String bedrock = "bedrock.";
    private final String java = "java.";
    private final String cracked = "cracked.";

    private String formatConfigName(String name) {
        if (name.startsWith(".")) {
            return this.bedrock + name.substring(1);
        } else if (name.startsWith("*")) {
            return this.cracked + name.substring(1);
        } else {
            return this.java + name;
        }
    }

    public void registerUUID(UUID uuid, String playerName) {
        String tmpName = this.formatConfigName(playerName);

        this.uuidConfig.reload();
        List<String> playerNames = this.uuidConfig.getKeys(true);
        for (String forPlayerName : playerNames) {
            String tmp = forPlayerName + ".";
            if (tmp.equals(this.bedrock) || tmp.equals(this.java) || tmp.equals(this.cracked)) {
                continue;
            }

            String uuidString = this.uuidConfig.getString(forPlayerName);
            if (uuidString.equals(uuid.toString())) {
                this.uuidConfig.set(forPlayerName, null);
            }
        }

        this.uuidConfig.set(tmpName, uuid.toString());
        this.uuidConfig.save();
    }

    public UUID playerNameToUUID(String playerName) {
        Optional<Player> player = EasyVelocity.getServer().getPlayer(playerName);
        if (player.isPresent()) {
            this.registerUUID(player.get().getUniqueId(), playerName);
            return player.get().getUniqueId();
        }

        String tmpName = this.formatConfigName(playerName);

        this.uuidConfig.reload();
        if (this.uuidConfig.contains(tmpName)) {
            String tmp = this.uuidConfig.getString(tmpName);
            if (tmp != null) {
                try {
                    return UUID.fromString(tmp);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        UUID uuid = UUIDFetcher.getUUID(playerName);
        if (uuid != null) {
            this.registerUUID(uuid, playerName);

            return uuid;
        }

        return null;
    }

    public String getPlayerName(UUID uuid) {
        Optional<Player> player = EasyVelocity.getServer().getPlayer(uuid);
        if (player.isPresent()) {
            this.registerUUID(player.get().getUniqueId(), player.get().getUsername());
            return player.get().getUsername();
        }

        this.uuidConfig.reload();
        List<String> playerNames = this.uuidConfig.getKeys(true);
        for (String playerName : playerNames) {
            String tmp = playerName + ".";
            if (tmp.equals(this.bedrock) || tmp.equals(this.java) || tmp.equals(this.cracked)) {
                continue;
            }

            String uuidString = this.uuidConfig.getString(playerName);
            if (uuidString.equals(uuid.toString())) {
                if (playerName.startsWith(this.bedrock)) {
                    return playerName.substring(this.bedrock.length()) + ".";
                } else if (playerName.startsWith(this.java)) {
                    return playerName.substring(this.java.length());
                } else if (playerName.startsWith(this.cracked)) {
                    return playerName.substring(this.cracked.length()) + "*";
                } else {
                    EasyVelocity.getLogger().error("Something is wrong in the UUID config!!! " + playerName);
                    return null;
                }
            }
        }

        String name = UUIDFetcher.getName(uuid);
        if (name != null) {
            this.registerUUID(uuid, name);

            return name;
        }

        return null;
    }
}
