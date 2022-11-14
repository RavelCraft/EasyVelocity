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

public class VelocityKickCommand extends EasyVelocityCommand {
    public VelocityKickCommand() {
        super("proxykick", "easyvelocity.proxykick", "pkick");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(PlayerMessage.COMMAND_KICK_HELP);
            return;
        }

        Optional<Player> player = EasyVelocity.getServer().getPlayer(args[0]);
        if (player.isPresent()) {
            player.get().disconnect(PlayerMessage.formatMessage(PlayerMessage.COMMAND_KICK_MESSAGE, player.get()));
        } else {
            sender.sendMessage(PlayerMessage.COMMAND_KICK_PLAYER_NOT_FOUND);
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
