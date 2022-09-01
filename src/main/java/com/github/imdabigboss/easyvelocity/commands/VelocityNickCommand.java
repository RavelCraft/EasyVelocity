package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VelocityNickCommand extends EasyVelocityCommand {
    public VelocityNickCommand() {
        super("proxynick", "easyvelocity.proxynick", "pnick");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /proxynick <player> <player>/stop");
            return;
        }

        UUID playerUUID = EasyVelocity.getUUIDManager().playerNameToUUID(args[0]);
        if (playerUUID == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        UUID originalUUID = EasyVelocity.getNickManager().getOriginalPlayer(playerUUID);
        if (originalUUID == null) {
            originalUUID = playerUUID;
        }

        if (args[1].equalsIgnoreCase("stop")) {
            UUID nickedUUID = EasyVelocity.getNickManager().getNick(originalUUID);
            EasyVelocity.getNickManager().setNick(originalUUID, null);

            Optional<Player> player = EasyVelocity.getServer().getPlayer(playerUUID);
            if (player.isPresent()) {
                player.get().disconnect(Component.text("You have been un-nicked."));
            }
            player = EasyVelocity.getServer().getPlayer(nickedUUID);
            if (player.isPresent()) {
                player.get().disconnect(Component.text("You have been un-nicked."));
            }
        } else {
            UUID newUuid = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);

            if (newUuid == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }

            EasyVelocity.getNickManager().setNick(originalUUID, newUuid);
            sender.sendMessage(ChatColor.AQUA + "Should be done! Rejoin if needed.");
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                cmds.add(player.getUsername());
            }
        }
        return cmds;
    }
}
