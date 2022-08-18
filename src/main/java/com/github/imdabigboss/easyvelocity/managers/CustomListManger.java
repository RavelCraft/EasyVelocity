package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class CustomListManger {
    private final String TEMPLATE_HEADER = ChatColor.YELLOW +
            "---- " + ServerInfo.SERVER_NAME + " ----\n" +
            "There are %proxy_online%/" + ServerInfo.MAX_PLAYERS + " players online!\n" +
            "With %server_online% on %server_name%.";
    private final Component TEMPLATE_FOOTER = Component.text(ChatColor.YELLOW + "--------" + "-".repeat(ServerInfo.SERVER_NAME.length()));

    public void broadcastCustomList() {
        int online = EasyVelocity.getServer().getPlayerCount();

        String header = TEMPLATE_HEADER.replace("%proxy_online%", Integer.toString(online));

        for (Player player : EasyVelocity.getServer().getAllPlayers()) {
            String server = "ERROR";
            int serverOnline = -1;

            if (player.getCurrentServer().isPresent()) {
                server = player.getCurrentServer().get().getServerInfo().getName();
                serverOnline = player.getCurrentServer().get().getServer().getPlayersConnected().size();
            }

            Component headerComponent = Component.text(header
                    .replace("%server_online%", Integer.toString(serverOnline))
                    .replace("%server_name%", server));

            player.sendPlayerListHeaderAndFooter(headerComponent, TEMPLATE_FOOTER);
        }
    }
}
