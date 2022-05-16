package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.proxy.Player;

import java.util.*;

public class RanksManager {
    private final PluginConfig config = EasyVelocity.getConfig("ranks");

    private final Map<String, String> allRanks = new HashMap<>();
    private final Map<UUID, String> playerRanks = new HashMap<>();

    public RanksManager() {
        this.loadRanks();
        this.displayAllRank();
    }

    public void loadRanks() {
        for (String rank : config.getKeys(false)) {
            if (config.get(rank) == null) {
                continue;
            }

            String color = ChatColor.WHITE;
            if (config.contains(rank + ".color")) {
                color = ChatColor.stingToChatColor(config.getString(rank + ".color"));
            }

            if (config.contains(rank + ".players")) {
                Object obj = config.get(rank + ".players");

                if (obj != null) {
                    if (obj instanceof List) {
                        for (String uuidString : this.config.getStringList(rank + ".players")) {
                            try {
                                UUID uuid = UUID.fromString(uuidString);
                                playerRanks.put(uuid, rank);
                            } catch (Exception e) {
                                EasyVelocity.getLogger().error("Failed to parse UUID from config: " + uuidString);
                            }
                        }
                    }
                }
            }

            allRanks.put(rank, color);
        }
    }

    private void displayRank(UUID uuid, String rank) {
        Optional<Player> optionalPlayer = EasyVelocity.getServer().getPlayer(uuid);
        if (!optionalPlayer.isPresent()) {
            return;
        }
        Player player = optionalPlayer.get();

        if (allRanks.containsKey(rank)) {
            String color = allRanks.get(rank);
            EasyVelocity.getPluginMessageManager().sendPlayerCommand(player, "setdisplayname", uuid.toString() + ";" + rank + ";" + color, "EasyCMD");
        } else {
            EasyVelocity.getPluginMessageManager().sendPlayerCommand(player, "setdisplayname", uuid.toString() + ";none;" + ChatColor.RESET, "EasyCMD");
        }
    }

    public void displayRank(UUID uuid) {
        this.displayRank(uuid, this.getRank(uuid));
    }

    public void displayAllRank() {
        for (Player player : EasyVelocity.getServer().getAllPlayers()) {
            this.displayRank(player.getUniqueId(), this.getRank(player.getUniqueId()));
        }
    }

    public boolean setPlayerRank(UUID uuid, String rank) {
        if (rank.equals("none")) {
            if (playerRanks.containsKey(uuid)) {
                String playerRank = playerRanks.get(uuid);
                playerRanks.remove(uuid);

                List<String> players = config.getStringList(playerRank + ".players");
                players.remove(uuid.toString());

                config.set(playerRank + ".players", players);
                config.save();

                if (EasyVelocity.getServer().getPlayer(uuid).isPresent()) {
                    this.displayRank(uuid, "none");
                }
            }
            return true;
        }

        if (allRanks.containsKey(rank)) {
            if (playerRanks.containsKey(uuid)) {
                if (playerRanks.get(uuid).equals(rank)) {
                    return false;
                }

                String playerRank = playerRanks.get(uuid);
                playerRanks.remove(uuid);

                List<String> players = config.getStringList(playerRank + ".players");
                players.remove(uuid.toString());

                config.set(playerRank + ".players", players);
                config.save();
            }

            playerRanks.put(uuid, rank);

            List<String> players = config.getStringList(rank + ".players");
            players.add(uuid.toString());

            config.set(rank + ".players", players);
            config.save();

            if (EasyVelocity.getServer().getPlayer(uuid).isPresent()) {
                this.displayRank(uuid, rank);
            }

            return true;
        } else {
            return false;
        }
    }

    public String getRank(UUID uuid) {
        if (playerRanks.containsKey(uuid)) {
            return playerRanks.get(uuid);
        }
        return "";
    }

    public void reloadRanks() {
        this.allRanks.clear();
        this.playerRanks.clear();

        this.loadRanks();
        this.displayAllRank();
    }

    public Set<Map.Entry<String, String>> getRanks() {
        return allRanks.entrySet();
    }

    public List<String> getRankNames() {
        return new ArrayList<>(allRanks.keySet());
    }

    public boolean addRank(String rank, String color) {
        if (!allRanks.containsKey(rank)) {
            allRanks.put(rank, ChatColor.stingToChatColor(color));

            config.set(rank + ".color", color);
            config.set(rank + ".players", new ArrayList<>());
            config.save();

            return true;
        } else {
            return false;
        }
    }

    public boolean removeRank(String rank) {
        if (allRanks.containsKey(rank)) {
            allRanks.remove(rank);

            config.set(rank, null);
            config.save();

            for (Map.Entry<UUID, String> entry : playerRanks.entrySet()) {
                if (entry.getValue().equals(rank)) {
                    playerRanks.remove(entry.getKey());
                    this.displayRank(entry.getKey(), "none");
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
