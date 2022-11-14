package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.velocitypowered.api.util.GameProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CrackedPlayerManager {
    private final PluginConfig config = EasyVelocity.getConfig("cracked");

    private final List<CrackedPlayerSession> crackedAuth = new ArrayList<>();

    public GameProfile getCrackedProfile(String name) {
        int index = -1;
        for (int i = 0; i < crackedAuth.size(); i++) {
            CrackedPlayerSession entry = this.crackedAuth.get(i);
            if (entry.getName().equals(name)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        CrackedPlayerSession entry = this.crackedAuth.get(index);
        this.crackedAuth.remove(index);

        if (System.currentTimeMillis() > entry.getTime() + (20 * 1000)) {
            return null;
        }

        if (!this.config.contains(name)) {
            return null;
        }

        UUID profileUUID = UUID.fromString(this.config.getString(name + ".uuid"));
        EasyVelocity.getUUIDManager().registerUUID(profileUUID, name);

        return new GameProfile(profileUUID, name, Collections.singletonList(new GameProfile.Property("textures", "", "")));
    }

    public boolean crackedPlayerLogin(String name, String username, String password) {
        if (!this.config.contains(name)) {
            return false;
        }

        if (!this.config.getString(name + ".username").equals(username) || !this.config.getString(name + ".password").equals(password)) {
            return false;
        }

        this.crackedAuth.add(new CrackedPlayerSession(name, System.currentTimeMillis()));
        return true;
    }

    public static class CrackedPlayerSession {
        private final String name;
        private final long time;

        public CrackedPlayerSession(String name, long time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return this.name;
        }

        public long getTime() {
            return this.time;
        }
    }
}
