package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LobbyCommand extends EasyVelocityCommand {
    public LobbyCommand() {
        super("lobby", "", "l", "hub");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (sender.isConsole()) {
            sender.sendMessage(PlayerMessage.COMMAND_MUST_BE_PLAYER);
            return;
        }

        Player player = sender.getPlayer();
        if (player.getCurrentServer().isPresent()) {
            if (player.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(ServerInfo.LOBBY_SERVER_NAME)) {
                sender.sendMessage(PlayerMessage.COMMAND_LOBBY_ALREADY_IN);
                return;
            }
        }

        Optional<RegisteredServer> lobbyServer = EasyVelocity.getServer().getServer(ServerInfo.LOBBY_SERVER_NAME);
        if (lobbyServer.isPresent()) {
            player.createConnectionRequest(lobbyServer.get()).connect().thenAccept(connection -> {
                if (connection.isSuccessful()) {
                    sender.sendMessage(PlayerMessage.COMMAND_LOBBY_SUCCESS);
                } else {
                    sender.sendMessage(PlayerMessage.COMMAND_LOBBY_ERROR);
                }
            });
        } else {
            sender.sendMessage(PlayerMessage.COMMAND_LOBBY_NOT_FOUND);
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
