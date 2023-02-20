package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SendCommand extends EasyVelocityCommand {
    public SendCommand() {
        super("sendserver", "", "send");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (sender.isConsole()) {
            sender.sendMessage(PlayerMessage.COMMAND_MUST_BE_PLAYER);
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(PlayerMessage.COMMAND_SEND_HELP);
            return;
        }

        Optional<Player> playerOptional = EasyVelocity.getServer().getPlayer(args[0]);
        if (!playerOptional.isPresent()) {
            sender.sendMessage(PlayerMessage.COMMAND_SEND_PLAYER_NOT_FOUND);
            return;
        }
        Player player = playerOptional.get();

        if (player.getCurrentServer().isPresent()) {
            if (player.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(args[1])) {
                sender.sendMessage(PlayerMessage.COMMAND_SEND_ERROR);
                return;
            }
        }

        Optional<RegisteredServer> server = EasyVelocity.getServer().getServer(args[1]);
        if (server.isPresent()) {
            player.createConnectionRequest(server.get()).connect().thenAccept(connection -> {
                if (connection.isSuccessful()) {
                    sender.sendMessage(PlayerMessage.COMMAND_SEND_SUCCESS);
                } else {
                    sender.sendMessage(PlayerMessage.COMMAND_SEND_ERROR);
                }
            });
        } else {
            sender.sendMessage(PlayerMessage.COMMAND_SEND_SERVER_NOT_FOUND);
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                cmds.add(player.getUsername());
            }
        } else if (args.length == 2) {
            for (RegisteredServer server : EasyVelocity.getServer().getAllServers()) {
                cmds.add(server.getServerInfo().getName());
            }
        }
        return cmds;
    }
}
