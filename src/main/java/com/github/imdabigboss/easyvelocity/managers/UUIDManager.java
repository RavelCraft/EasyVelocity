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

    public void registerUUID(UUID uuid, String playerName) {
        String tmpName;
        if (playerName.startsWith(".")) {
            tmpName = this.bedrock + playerName.substring(1);
        } else {
            tmpName = this.java + playerName;
        }

        this.uuidConfig.reload();
        List<String> playerNames = this.uuidConfig.getKeys(true);
        for (String forPlayerName : playerNames) {
            String tmp = forPlayerName + ".";
            if (tmp.equals(this.bedrock) || tmp.equals(this.java)) {
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

        String tmpName;
        if (playerName.startsWith(".")) {
            tmpName = this.bedrock + playerName.substring(1);
        } else {
            tmpName = this.java + playerName;
        }

        this.uuidConfig.reload();
        if (this.uuidConfig.contains(tmpName)) {
            String tmp = this.uuidConfig.getString(tmpName);
            if (tmp != null) {
                return UUID.fromString(tmp);
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
            if (tmp.equals(this.bedrock) || tmp.equals(this.java)) {
                continue;
            }

            String uuidString = this.uuidConfig.getString(playerName);
            if (uuidString.equals(uuid.toString())) {
                if (playerName.startsWith(this.bedrock)) {
                    return playerName.substring(7);
                } else if (playerName.startsWith(this.java)) {
                    return playerName.substring(5);
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
