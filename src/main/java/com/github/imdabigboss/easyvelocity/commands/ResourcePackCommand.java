package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.github.imdabigboss.easyvelocity.utils.TexturePack;
import com.velocitypowered.api.proxy.Player;

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
                sender.sendMessage(PlayerMessage.COMMAND_PACK_GENERATED);
            }).start();
        } else if (args[0].equalsIgnoreCase("send")) {
            if (args.length == 1) {
                if (sender.isConsole()) {
                    sender.sendMessage(PlayerMessage.COMMAND_PACK_SPECIFY_PLAYER);
                } else {
                    TexturePack.sendTexturePackToPlayer(sender.getPlayer());
                    sender.sendMessage(PlayerMessage.COMMAND_PACK_SENT);
                }
            } else if (args.length == 2) {
                Optional<Player> player = EasyVelocity.getServer().getPlayer(args[1]);
                if (!player.isPresent()) {
                    sender.sendMessage(PlayerMessage.COMMAND_PACK_NO_PLAYER);
                    return;
                }

                TexturePack.sendTexturePackToPlayer(player.get());
                sender.sendMessage(PlayerMessage.COMMAND_PACK_SENT);
            }
        } else if (args[0].equalsIgnoreCase("sendall")) {
            for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                TexturePack.sendTexturePackToPlayer(player);
            }
            sender.sendMessage(PlayerMessage.COMMAND_PACK_SENT_ALL);
        } else {
            this.sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(PlayerMessage.COMMAND_PACK_HELP);
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
