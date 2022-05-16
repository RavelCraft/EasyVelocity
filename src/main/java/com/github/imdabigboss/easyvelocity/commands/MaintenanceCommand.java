package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand extends EasyVelocityCommand {
    public MaintenanceCommand() {
        super("proxymaintenance", "easyvelocity.proxymaintenance", "pmaintenance");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length < 1) {
            sendHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("on")) {
            EasyVelocity.getMaintenanceManager().setMaintenance(true);
            sender.sendMessage(ChatColor.AQUA + "Maintenance mode has been enabled!");
        } else if (args[0].equalsIgnoreCase("off")) {
            EasyVelocity.getMaintenanceManager().setMaintenance(false);
            sender.sendMessage(ChatColor.AQUA + "Maintenance mode has been disabled!");
        } else if (args[0].equalsIgnoreCase("bypass")) {
            new Thread(() -> {
                if (args.length < 2) {
                    sender.sendMessage("The correct usage is '/maintenance bypass <player name> true/false'.");
                    return;
                } else if (args.length < 3) {
                    if (EasyVelocity.getMaintenanceManager().canPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]))) {
                        sender.sendMessage(ChatColor.AQUA + args[1] + " can bypass maintenance.");
                    } else {
                        sender.sendMessage(ChatColor.AQUA + args[1] + " can't bypass maintenance.");
                    }
                    return;
                }

                if (args[2].equalsIgnoreCase("true")) {
                    EasyVelocity.getMaintenanceManager().setPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), true);
                    sender.sendMessage(ChatColor.AQUA + args[1] + " can now bypass maintenance mode.");
                } else if (args[2].equalsIgnoreCase("false")) {
                    EasyVelocity.getMaintenanceManager().setPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), false);
                    sender.sendMessage(ChatColor.AQUA + args[1] + " can't bypass maintenance mode.");
                } else {
                    sendHelp(sender);
                }
            }).start();
        } else {
            sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage("Maintenance mode: " + EasyVelocity.getMaintenanceManager().isMaintenance() + ".");
        sender.sendMessage("You may set maintenance mode using 'on' or 'off'.");
        sender.sendMessage("Set maintenance bypasses with 'bypass <player name> true/false'.");
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("on");
            cmds.add("off");
            cmds.add("bypass");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("bypass")) {
                for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                    cmds.add(player.getUsername());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("bypass")) {
                cmds.add("true");
                cmds.add("false");
            }
        }
        return cmds;
    }
}
