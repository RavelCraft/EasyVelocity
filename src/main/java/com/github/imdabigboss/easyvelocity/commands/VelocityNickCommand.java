package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
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
            sender.sendMessage(PlayerMessage.COMMAND_NICK_HELP);
            return;
        }

        UUID playerUUID = EasyVelocity.getUUIDManager().playerNameToUUID(args[0]);
        if (playerUUID == null) {
            sender.sendMessage(PlayerMessage.COMMAND_NICK_PLAYER_NOT_FOUND);
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
                player.get().disconnect(PlayerMessage.formatMessage(PlayerMessage.COMMAND_NICK_STOP, player.get()));
            }
            player = EasyVelocity.getServer().getPlayer(nickedUUID);
            if (player.isPresent()) {
                player.get().disconnect(PlayerMessage.formatMessage(PlayerMessage.COMMAND_NICK_STOP, player.get()));
            }
        } else {
            UUID newUuid = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);

            if (newUuid == null) {
                sender.sendMessage(PlayerMessage.COMMAND_NICK_PLAYER_NOT_FOUND);
                return;
            }

            EasyVelocity.getNickManager().setNick(originalUUID, newUuid);
            sender.sendMessage(PlayerMessage.COMMAND_NICK_REJOIN);
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
