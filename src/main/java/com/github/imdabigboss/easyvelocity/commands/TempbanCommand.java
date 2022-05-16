package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.Utils;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TempbanCommand extends EasyVelocityCommand {
    public TempbanCommand() {
        super("tempban", "easyvelocity.tempban");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length < 1) {
            this.sendHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("ban")) {
            if (args.length < 3) {
                this.sendHelp(sender);
                return;
            }

            int time;
            try {
                time = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid time: " + args[2] + ". That is not a number. Go back to preschool.");
                return;
            }

            UUID uuid = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);
            if (uuid == null) {
                sender.sendMessage(ChatColor.RED + "That player doesn't exist. Let me go back to sleep.");
                return;
            }

            String reason;
            if (args.length > 3) {
                StringBuilder sb = new StringBuilder();
                for (int i = 3; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                reason = sb.toString().trim();
            } else {
                reason = "No reason was given.";
            }

            new Thread(() -> {
                EasyVelocity.getBanManager().banPlayer(uuid, reason, time);
                sender.sendMessage(ChatColor.AQUA + args[1] + " has been banned for " + time + " days. ");
            }).start();
        } else if (args[0].equalsIgnoreCase("unban")) {
            if (args.length != 2) {
                this.sendHelp(sender);
                return;
            }

            new Thread(() -> {
                UUID uuid = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);
                if (uuid == null) {
                    sender.sendMessage(ChatColor.RED + "That player doesn't exist.");
                    return;
                }

                if (EasyVelocity.getBanManager().isPlayerBanned(uuid)) {
                    EasyVelocity.getBanManager().unbanPlayer(uuid);
                    sender.sendMessage(ChatColor.AQUA + args[1] + " has been unbanned.");
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is not banned.");
                }
            }).start();
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length != 1) {
                this.sendHelp(sender);
                return;
            }

            new Thread(() -> {
                StringBuilder sb = new StringBuilder("Banned players:");
                for (String ban : EasyVelocity.getBanManager().getBans()) {
                    String[] split = ban.split(":");

                    String player = EasyVelocity.getUUIDManager().getPlayerName(UUID.fromString(split[0]));

                    long unban;
                    try {
                        unban = Long.parseLong(split[1]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    sb.append("\n - ").append(player).append(" banned until ").append(Utils.epochToStringDate(unban)).append(". Reason: ");
                    for (int i = 2; i < split.length; i++) {
                        sb.append(split[i]).append(" ");
                    }
                }

                sender.sendMessage(sb.toString());
            }).start();
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage:\n" +
                " - /tempban ban <player> <time: days> [reason]\n" +
                " - /tempban unban <player>\n" +
                " - /tempban list");
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("ban");
            cmds.add("unban");
            cmds.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("unban")) {
                for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                    cmds.add(player.getUsername());
                }
            }
        }
        return cmds;
    }
}
