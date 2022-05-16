package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LobbyCommand extends EasyVelocityCommand {
    public LobbyCommand() {
        super("lobby", "", "l");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        Player player = sender.getPlayer();
        if (player != null) {
            if (player.getCurrentServer().isPresent()) {
                if (player.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(ServerInfo.LOBBY_SERVER_NAME)) {
                    sender.sendMessage(ChatColor.RED + "You are already in the lobby!");
                    return;
                }
            }

            Optional<RegisteredServer> lobbyServer = EasyVelocity.getServer().getServer(ServerInfo.LOBBY_SERVER_NAME);
            if (lobbyServer.isPresent()) {
                player.createConnectionRequest(lobbyServer.get()).connect().thenAccept(connection -> {
                    if (connection.isSuccessful()) {
                        sender.sendMessage(ChatColor.AQUA + "You have been sent to the lobby!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "An error occurred while sending you to the lobby...");
                    }
                });
            } else {
                sender.sendMessage(ChatColor.RED + "There is no lobby server...");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You can't use this command from console!");
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
