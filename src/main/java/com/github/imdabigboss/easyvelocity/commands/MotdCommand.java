package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;

import java.util.Collections;
import java.util.List;

public class MotdCommand extends EasyVelocityCommand {
    public MotdCommand() {
        super("setmotd", "easyvelocity.motd", "motd");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(PlayerMessage.COMMAND_MOTD_HELP);
            return;
        }

        String motd = String.join(" ", args);
        motd = motd.replace("&", "§");

        EasyVelocity.setMotdMessage(motd);
        EasyVelocity.getConfig().set("motdMessage", motd);
        EasyVelocity.getConfig().save();

        sender.sendMessage(PlayerMessage.COMMAND_MOTD_UPDATED);
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
