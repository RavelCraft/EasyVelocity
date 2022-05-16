package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;

public class WhitelistCommand extends EasyVelocityCommand {
    public WhitelistCommand() {
        super("proxywhitelist", "easyvelocity.whitelist", "pwhitelist");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /proxywhitelist <add/remove/enable> <player/true/false> [server]");
        }

        new Thread(() -> {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (EasyVelocity.getWhitelistManager().addPlayer(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]))) {
                        sender.sendMessage(ChatColor.AQUA + "Added " + args[1] + " to the whitelist.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to add " + args[1] + " to the whitelist.");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (EasyVelocity.getWhitelistManager().removePlayer(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]))) {
                        sender.sendMessage(ChatColor.AQUA + "Removed " + args[1] + " from the whitelist.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to remove " + args[1] + " from the whitelist.");
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (EasyVelocity.getWhitelistManager().addPlayer(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), args[2])) {
                        sender.sendMessage(ChatColor.AQUA + "Added " + args[1] + " to the whitelist on " + args[2] + ".");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to add " + args[1] + " to the whitelist on " + args[2] + ".");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (EasyVelocity.getWhitelistManager().removePlayer(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), args[2])) {
                        sender.sendMessage(ChatColor.AQUA + "Removed " + args[1] + " from the whitelist on " + args[2] + ".");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Failed to remove " + args[1] + " from the whitelist on " + args[2] + ".");
                    }
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (args[1].equalsIgnoreCase("true")) {
                        EasyVelocity.getWhitelistManager().setEnabled(args[2], true);
                        sender.sendMessage(ChatColor.AQUA + "Whitelist enabled.");
                    } else if (args[1].equalsIgnoreCase("false")) {
                        EasyVelocity.getWhitelistManager().setEnabled(args[2], false);
                    }
                }
            }
        }).start();
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("add");
            cmds.add("remove");
            cmds.add("enable");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("enable")) {
                cmds.add("true");
                cmds.add("false");
            } else {
                for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                    cmds.add(player.getUsername());
                }
            }
        } else if (args.length == 3) {
            for (RegisteredServer server : EasyVelocity.getServer().getAllServers()) {
                cmds.add(server.getServerInfo().getName());
            }
        }
        return cmds;
    }
}
