package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;

import java.util.Collections;
import java.util.List;

public class InfoCommand extends EasyVelocityCommand {
    public InfoCommand() {
        super("serverinfo", "", "info");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        sender.sendMessage(PlayerMessage.COMMAND_INFO);
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
