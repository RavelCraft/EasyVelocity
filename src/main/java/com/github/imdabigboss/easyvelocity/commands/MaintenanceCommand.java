package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
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
            sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_ENABLED);
        } else if (args[0].equalsIgnoreCase("off")) {
            EasyVelocity.getMaintenanceManager().setMaintenance(false);
            sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_DISABLED);
        } else if (args[0].equalsIgnoreCase("bypass")) {
            new Thread(() -> {
                if (args.length < 2) {
                    sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_HELP);
                } else if (args.length == 2) {
                    if (EasyVelocity.getMaintenanceManager().canPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]))) {
                        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_CAN_BYPASS, args[1]);
                    } else {
                        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_CANNOT_BYPASS, args[1]);
                    }
                } else {
                    if (args[2].equalsIgnoreCase("true")) {
                        EasyVelocity.getMaintenanceManager().setPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), true);
                        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_CAN_BYPASS, args[1]);
                    } else if (args[2].equalsIgnoreCase("false")) {
                        EasyVelocity.getMaintenanceManager().setPlayerBypass(EasyVelocity.getUUIDManager().playerNameToUUID(args[1]), false);
                        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_CANNOT_BYPASS, args[1]);
                    } else {
                        sendHelp(sender);
                    }
                }
            }).start();
        } else {
            sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_STATUS, EasyVelocity.getMaintenanceManager().isMaintenance() ? "enabled" : "disabled");
        sender.sendMessage(PlayerMessage.COMMAND_MAINTENANCE_HELP);
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
