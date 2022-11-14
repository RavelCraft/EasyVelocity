package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.List;

public class ConfigCommand extends EasyVelocityCommand {
    public ConfigCommand() {
        super("proxyconfig", "easyvelocity.proxyconfig", "pconfig");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length != 1) {
            this.sendHelp(sender);
            return;
        }

        if (args[0].equals("reload")) {
            EasyVelocity.getConfigManager().reloadConfigs();
            sender.sendMessage(PlayerMessage.COMMAND_CONFIG_RELOADED);
        } else {
            this.sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(PlayerMessage.COMMAND_CONFIG_HELP);
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
