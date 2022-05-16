package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.github.imdabigboss.easyvelocity.utils.Utils;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BanManager {
    private final PluginConfig config = EasyVelocity.getConfig("bans");

    public List<String> getBans() {
        if (config.contains("bans")) {
            return config.getStringList("bans");
        } else {
            return new ArrayList<>();
        }
    }

    private String constructBan(long unban, String reason) {
        return  ChatColor.RED + "You are banned until " + Utils.epochToStringDate(unban) + ".\nYou may appeal at ravelcraft@connexal.com or in #support on Discord.\n\nReason: " + reason + ".";
    }

    public void banPlayer(UUID uuid, String reason, int days) {
        long unban = System.currentTimeMillis() + (days * 86400000L);

        List<String> bans = this.getBans();

        bans.add(uuid + ":" + unban + ":" + reason);

        config.set("bans", bans);
        config.save();

        Optional<Player> player = EasyVelocity.getServer().getPlayer(uuid);
        if (player.isPresent()) {
            player.get().disconnect(Component.text(this.constructBan(unban, reason)));
        }
    }

    public String getPlayerBanMessage(UUID uuid) {
        List<String> bans = this.getBans();

        for (String ban : bans) {
            String[] split = ban.split(":");

            if (!split[0].equals(uuid.toString())) {
                continue;
            }

            long unban;
            try {
                unban = Long.parseLong(split[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            if (System.currentTimeMillis() < unban) {
                String reason;
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    sb.append(split[i]).append(" ");
                }
                reason = sb.toString().trim();

                return this.constructBan(unban, reason);
            } else {
                bans.remove(ban);

                config.set("bans", bans);
                config.save();

                return null;
            }
        }

        return null;
    }

    public void unbanPlayer(UUID uuid) {
        List<String> bans = this.getBans();
        List<String> newBans = new ArrayList<>();

        for (String ban : bans) {
            String[] split = ban.split(":");

            if (!split[0].equals(uuid.toString())) {
                newBans.add(ban);
            }
        }

        config.set("bans", newBans);
        config.save();
    }

    public boolean isPlayerBanned(UUID uuid) {
        String message = this.getPlayerBanMessage(uuid);
        return message != null;
    }
}
