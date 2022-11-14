package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class CustomListManger {
    private void broadcastCustomListInt(Player player, String proxyOnline) {
        String server = "ERROR";
        int serverOnlineInt = -1;
        if (player.getCurrentServer().isPresent()) {
            server = player.getCurrentServer().get().getServerInfo().getName();
            serverOnlineInt = player.getCurrentServer().get().getServer().getPlayersConnected().size();
        }

        String serverOnline = Integer.toString(serverOnlineInt);

        player.sendPlayerListHeaderAndFooter(
                PlayerMessage.formatMessage(PlayerMessage.PLAYER_LIST_HEADER, player, proxyOnline, serverOnline, server),
                PlayerMessage.formatMessage(PlayerMessage.PLAYER_LIST_FOOTER, player)
        );
    }

    public void broadcastCustomList() {
        int proxyOnlineInt = EasyVelocity.getServer().getPlayerCount();
        String proxyOnline = Integer.toString(proxyOnlineInt);

        for (Player player : EasyVelocity.getServer().getAllPlayers()) {
            broadcastCustomListInt(player, proxyOnline);
        }
    }

    public void broadcastCustomList(Player player) {
        int proxyOnlineInt = EasyVelocity.getServer().getPlayerCount();
        String proxyOnline = Integer.toString(proxyOnlineInt);
        broadcastCustomListInt(player, proxyOnline);
    }
}
