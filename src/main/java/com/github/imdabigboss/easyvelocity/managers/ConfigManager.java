package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final Map<String, PluginConfig> configs = new HashMap<>();

    public ConfigManager() {
        this.reloadConfigs();
    }

    public PluginConfig getConfig(String name) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        } else {
            PluginConfig config = new PluginConfig(Paths.get("plugins/" + PluginInfo.NAME + "/" + name + ".yml"));
            config.save();

            configs.put(name, config);

            return config;
        }
    }

    public void createConfig(Path path, String name) {
        if (configs.containsKey(name)) {
            throw new IllegalArgumentException("Config already exists!");
        } else {
            if (Files.notExists(path)) {
                try {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                } catch (IOException e) {
                    EasyVelocity.getLogger().error("Could not create config file at " + path.toAbsolutePath(), e);
                }
            }

            PluginConfig config = new PluginConfig(path);
            config.save();

            configs.put(name, config);
        }
    }

    public void reloadConfigs() {
        configs.clear();

        PluginConfig config = new PluginConfig(Paths.get("plugins/" + PluginInfo.NAME + "/config.yml"));
        config.save();
        configs.put("config", config);
    }

    public void saveConfigs() {
        for (PluginConfig config : configs.values()) {
            config.save();
        }
    }
}
