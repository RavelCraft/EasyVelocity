package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.Player;

import java.util.*;

public class RanksCommand extends EasyVelocityCommand {
    public RanksCommand() {
        super("ranks", "easyvelocity.ranks");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("get")) {
            if (args.length != 2) {
                sendHelp(sender);
                return;
            }

            new Thread(() -> {
                UUID found = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);
                if (found == null) {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_PLAYER_NOT_FOUND);
                    return;
                }

                String foundRank = EasyVelocity.getRanksManager().getRank(found);
                if (foundRank.equals("")) {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_NO_RANK, args[1]);
                } else {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_PLAYER_RANK, args[1], foundRank);
                }
            }).start();
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 3) {
                sendHelp(sender);
                return;
            }

            new Thread(() -> {
                UUID found = EasyVelocity.getUUIDManager().playerNameToUUID(args[1]);
                if (found == null) {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_PLAYER_NOT_FOUND);
                    return;
                }

                boolean managed = EasyVelocity.getRanksManager().setPlayerRank(found, args[2]);
                if (managed) {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_PLAYER_RANK_SET, args[1], args[2]);
                } else {
                    sender.sendMessage(PlayerMessage.COMMAND_RANKS_NO_RANK);
                }
            }).start();
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 3) {
                sendHelp(sender);
                return;
            }

            boolean managed = EasyVelocity.getRanksManager().addRank(args[1], args[2]);
            if (managed) {
                sender.sendMessage(PlayerMessage.COMMAND_RANKS_ADDED);
            } else {
                sender.sendMessage(PlayerMessage.COMMAND_RANKS_ALREADY_EXISTS);
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                sendHelp(sender);
                return;
            }

            boolean managed = EasyVelocity.getRanksManager().removeRank(args[1]);
            if (managed) {
                sender.sendMessage(PlayerMessage.COMMAND_RANKS_REMOVED);
            } else {
                sender.sendMessage(PlayerMessage.COMMAND_RANKS_NO_RANK);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            Set<Map.Entry<String, String>> ranks = EasyVelocity.getRanksManager().getRanks();
            if (ranks.size() == 0) {
                sender.sendMessage(PlayerMessage.COMMAND_RANKS_NO_RANKS);
            } else {
                StringBuilder message = new StringBuilder();
                for (Map.Entry<String, String> rank : ranks) {
                    message.append("\n").append(" - ").append(rank.getValue()).append(rank.getKey()).append(ChatColor.RESET);
                }

                sender.sendMessage(PlayerMessage.COMMAND_RANKS_LIST, message.toString());
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            EasyVelocity.getRanksManager().reloadRanks();

            sender.sendMessage(PlayerMessage.COMMAND_RANKS_RELOADED);
        } else {
            sendHelp(sender);
        }
    }

    private void sendHelp(EasyCommandSender sender) {
        sender.sendMessage(PlayerMessage.COMMAND_RANKS_HELP);
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("get");
            cmds.add("set");
            cmds.add("add");
            cmds.add("remove");
            cmds.add("reload");
            cmds.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("set")) {
                for (Player player : EasyVelocity.getServer().getAllPlayers()) {
                    cmds.add(player.getUsername());
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                cmds.add("<name>");
            } else if (args[0].equalsIgnoreCase("remove")) {
                cmds.addAll(EasyVelocity.getRanksManager().getRankNames());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                cmds.add("none");
                cmds.addAll(EasyVelocity.getRanksManager().getRankNames());
            } else if (args[0].equalsIgnoreCase("add")) {
                cmds.add("AQUA");
                cmds.add("BLACK");
                cmds.add("BLUE");
                cmds.add("DARK_AQUA");
                cmds.add("DARK_BLUE");
                cmds.add("DARK_GRAY");
                cmds.add("DARK_GREEN");
                cmds.add("DARK_PURPLE");
                cmds.add("DARK_RED");
                cmds.add("GOLD");
                cmds.add("GRAY");
                cmds.add("GREEN");
                cmds.add("LIGHT_PURPLE");
                cmds.add("RED");
                cmds.add("WHITE");
                cmds.add("YELLOW");
            }
        }
        return cmds;
    }
}
