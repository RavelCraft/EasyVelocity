package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.ServerUtils;

import java.util.Collections;
import java.util.List;

public class BroadcastCommand extends EasyVelocityCommand {
    public BroadcastCommand() {
        super("broadcast", "easyvelocity.broadcast", "psay");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        String message = ChatColor.BOLD + "[Server Broadcast] " + ChatColor.RESET + String.join(" ", args);

        ServerUtils.broadcast(message);
        if (sender.isConsole()) {
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
