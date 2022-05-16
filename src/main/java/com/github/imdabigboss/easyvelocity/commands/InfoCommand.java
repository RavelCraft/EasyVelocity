package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;

import java.util.Collections;
import java.util.List;

public class InfoCommand extends EasyVelocityCommand {
    public InfoCommand() {
        super("serverinfo", "", "info");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        String message = ChatColor.BOLD + "--- Server Information ---\n" + ChatColor.RESET +
                "Website: " + ChatColor.GOLD + PluginInfo.WEBSITE + ChatColor.RESET + "\n" +
                "Discord invites: " + ChatColor.GOLD + "sYWtr9E" + ChatColor.RESET + " or " + ChatColor.GOLD + "VFB6632" + ChatColor.RESET + "\n" +
                "Version: " + ChatColor.GOLD + EasyVelocity.getServer().getVersion().getName() + " - version " + EasyVelocity.getServer().getVersion().getVersion();

        sender.sendMessage(message);
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
