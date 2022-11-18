package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.proxy.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LanguageManager {
    private final Map<UUID, PlayerMessage.MessageLanguage> languages = new HashMap<>();
    private final PluginConfig config = EasyVelocity.getConfig("language");

    public LanguageManager() {
        for (String uuid : this.config.getKeys(false)) {
            String language = this.config.getString(uuid);

            try {
                languages.put(UUID.fromString(uuid), PlayerMessage.MessageLanguage.valueOf(language));
            } catch (IllegalArgumentException e) {
                EasyVelocity.getLogger().error("Invalid language or uuid in config: " + uuid + " - " + language);
            }
        }
    }

    public void setPlayerLanguage(Player player, PlayerMessage.MessageLanguage language) {
        this.languages.remove(player.getUniqueId());
        this.languages.put(player.getUniqueId(), language);

        this.config.set(player.getUniqueId().toString(), language.toString());
        this.config.save();

        EasyVelocity.getCustomListManger().broadcastCustomList(player);
        this.sendPlayerLanguageToPlugin(player);
    }

    public PlayerMessage.MessageLanguage getPlayerLanguage(Player player) {
        return this.languages.getOrDefault(player.getUniqueId(), null);
    }

    public void sendPlayerLanguageToPlugin(Player player) {
        EasyVelocity.getPluginMessageManager().sendCommand(player, "setplayerlanguage", player.getUniqueId().toString(), this.getPlayerLanguage(player).getCode());
    }
}
