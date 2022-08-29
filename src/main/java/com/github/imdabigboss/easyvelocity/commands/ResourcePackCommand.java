package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.TexturePack;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResourcePackCommand extends EasyVelocityCommand {
    public ResourcePackCommand() {
        super("proxyresourcepack", "easyvelocity.proxyresourcepack", "presourcepack", "ppack");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 1 && args.length != 2) {
            this.sendHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("generate")) {
            new Thread(() -> {
                TexturePack.generatePack();
                sender.sendMessage(ChatColor.AQUA + "Generated resource pack.");
            }).start();
        } else if (args[0].equalsIgnoreCase("send")) {
            if (args.length == 1) {
                if (sender.isConsole()) {
                    sender.sendMessage(ChatColor.RED + "You must specify a player.");
                } else {
                    TexturePack.sendTexturePackToPlayer(sender.getPlayer());
                    sender.sendMessage(ChatColor.AQUA + "Resource pack sent.");
                }
            } else if (args.length == 2) {
                Optional<Player> player = EasyVelocity.getServer().getPlayer(args[1]);
                if (!player.isPresent()) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return;
                }

                TexturePack.sendTexturePackToPlayer(player.get());
                sender.sendMessage(ChatColor.AQUA + "Resource pack sent.");
            }
        } else if (args[0].equalsIgnoreCase("sendall")) {
            for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                TexturePack.sendTexturePackToPlayer(player);
            }
            sender.sendMessage(ChatColor.AQUA + "Resource pack sent to all players.");
        } else {
            this.sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage("The correct usage is:\n - generate\n - send [player]\n - sendall");
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("send");
            cmds.add("sendall");
            cmds.add("generate");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("send")) {
                for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                    cmds.add(player.getUsername());
                }
            }
        }
        return cmds;
    }
}
