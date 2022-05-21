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

public class VelocityKickCommand extends EasyVelocityCommand {
    public VelocityKickCommand() {
        super("proxykick", "easyvelocity.proxykick", "pkick");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /proxykick <player>");
            return;
        }

        Optional<Player> player = EasyVelocity.getServer().getPlayer(args[0]);
        if (player.isPresent()) {
            player.get().disconnect(Component.text(ChatColor.RED + "You were kicked from the network."));
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found.");
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
