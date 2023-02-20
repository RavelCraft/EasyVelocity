package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.server.ServerInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerManageCommand extends EasyVelocityCommand {
    public ServerManageCommand() {
        super("servermanage", "easyvelocity.servermanage");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length < 1) {
            this.sendHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 4) {
                this.sendHelp(sender);
                return;
            }

            String name = args[1];
            String address = args[2];
            int port;
            try {
                port = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_ADD_ERROR);
                return;
            }

            InetSocketAddress socketAddress = new InetSocketAddress(address, port);
            ServerInfo serverInfo = new ServerInfo(name, socketAddress);

            if (EasyVelocity.getServerManager().addServer(serverInfo)) {
                sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_ADDED);
            } else {
                sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_ADD_ERROR);
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                this.sendHelp(sender);
                return;
            }

            if (EasyVelocity.getServerManager().removeServer(args[1])) {
                sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_REMOVED);
            } else {
                sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_REMOVE_ERROR);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length != 1) {
                this.sendHelp(sender);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (ServerInfo server : EasyVelocity.getServerManager().getServers()) {
                sb.append("\n - ").append(server.getName()).append(" (").append(server.getAddress().getHostString()).append(":").append(server.getAddress().getPort()).append(")");
            }

            sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_LIST, sb.toString());
        } else {
            this.sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(PlayerMessage.COMMAND_SERVER_MANAGE_HELP);
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("add");
            cmds.add("remove");
            cmds.add("list");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            cmds.add(args[1]);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("add")) {
            cmds.add("25565");
        }
        return cmds;
    }
}