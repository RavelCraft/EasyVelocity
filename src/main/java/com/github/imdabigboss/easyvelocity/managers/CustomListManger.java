package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

public class CustomListManger {
    private String templateHeader = ChatColor.YELLOW +
            "---- " + ServerInfo.SERVER_NAME + " ----\n" +
            "There are %proxy_online%/" + ServerInfo.MAX_PLAYERS + " players online!\n" +
            "With %server_online% on %server_name%.";
    private Component templateFooter = Component.text(ChatColor.YELLOW + "--------" + "-".repeat(ServerInfo.SERVER_NAME.length()));

    public void broadcastCustomList(Player thePlayer, RegisteredServer theirServer, int onlineChange) {
        int online = EasyVelocity.getServer().getPlayerCount();

        String header = templateHeader.replace("%proxy_online%", Integer.toString(online));

        for (Player player : EasyVelocity.getServer().getAllPlayers()) {
            String server = "ERROR";
            int serverOnline = -1;

            if (thePlayer != null && theirServer != null) {
                if (player.getUniqueId().equals(thePlayer.getUniqueId())) {
                    server = theirServer.getServerInfo().getName();
                    serverOnline = theirServer.getPlayersConnected().size() + onlineChange;
                } else if (player.getCurrentServer().isPresent()) {
                    RegisteredServer forPlayerServer = player.getCurrentServer().get().getServer();
                    String forPlayerServerName = player.getCurrentServer().get().getServerInfo().getName();

                    if (forPlayerServerName.equals(theirServer.getServerInfo().getName())) {
                        server = theirServer.getServerInfo().getName();
                        serverOnline = theirServer.getPlayersConnected().size() + onlineChange;
                    } else {
                        server = forPlayerServerName;
                        serverOnline = forPlayerServer.getPlayersConnected().size();
                    }
                }
            } else {
                if (player.getCurrentServer().isPresent()) {
                    server = player.getCurrentServer().get().getServerInfo().getName();
                    serverOnline = player.getCurrentServer().get().getServer().getPlayersConnected().size();
                }
            }

            Component headerComponent = Component.text(header
                    .replace("%server_online%", Integer.toString(serverOnline))
                    .replace("%server_name%", server));

            player.sendPlayerListHeaderAndFooter(headerComponent, templateFooter);
        }
    }

    public void broadcastCustomList(Player thePlayer, RegisteredServer theirServer) {
        this.broadcastCustomList(thePlayer, theirServer, 0);
    }

    public void broadcastCustomList() {
        this.broadcastCustomList(null, null, 0);
    }
}
