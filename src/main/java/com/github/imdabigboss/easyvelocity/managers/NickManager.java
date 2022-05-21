package com.github.imdabigboss.easyvelocity.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NickManager {
    private Map<UUID, UUID> nickToPlayer = new HashMap<>();
    private Map<UUID, UUID> playerToNick = new HashMap<>();

    public UUID getNick(UUID uuid) {
        return playerToNick.getOrDefault(uuid, null);
    }

    public UUID getOriginalPlayer(UUID nick) {
        return nickToPlayer.getOrDefault(nick, null);
    }

    public void setNick(UUID player, UUID nick) {
        if (nick == null) {
            UUID oldNick = playerToNick.get(player);
            playerToNick.remove(player);
            if (oldNick != null) {
                nickToPlayer.remove(oldNick);
            }
        } else {
            nickToPlayer.remove(nick);
            playerToNick.remove(player);

            nickToPlayer.put(nick, player);
            playerToNick.put(player, nick);
        }
    }
}
