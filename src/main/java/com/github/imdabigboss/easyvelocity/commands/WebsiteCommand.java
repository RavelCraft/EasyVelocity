package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.webserver.WebServer;

import java.util.ArrayList;
import java.util.List;

public class WebsiteCommand extends EasyVelocityCommand {
    public WebsiteCommand() {
        super("ravelwebsite", "easyvelocity.ravelwebsite");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (sender.isConsole()) {
            if (args.length != 1) {
                this.sendHelp(sender);
                return;
            }

            if (args[0].equals("reload")) {
                WebServer.reload();
                sender.sendMessage(ChatColor.GREEN + "Website reloaded!");
            } else {
                this.sendHelp(sender);
            }

            return;
        }

        //TODO
    }

    private void sendHelp(EasyCommandSender sender) {
        if (sender.isConsole()) {
            sender.sendMessage(ChatColor.RED + "Usage: /ravelwebsite reload");
            return;
        }

        //TODO
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (sender.isConsole()) {
            if (args.length == 1) {
                cmds.add("reload");
            }
        } else {
            //TODO
        }
        return cmds;
    }
}
