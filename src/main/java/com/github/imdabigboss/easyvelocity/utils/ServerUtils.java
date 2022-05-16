package com.github.imdabigboss.easyvelocity.utils;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class ServerUtils {
    public static void broadcast(String message) {
        for (Player player : EasyVelocity.getServer().getAllPlayers()) {
            player.sendMessage(Component.text(message));
        }
    }
}
