package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;

public class CrackedCommand extends EasyVelocityCommand {
    public CrackedCommand() {
        super("proxycracked", "easyvelocity.cracked", "pcracked");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /proxycracked <reload>");
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            EasyVelocity.getConfigManager().reloadConfigs();
            sender.sendMessage(ChatColor.GREEN + "Cracked config reloaded!");
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("reload");
        }
        return cmds;
    }
}
