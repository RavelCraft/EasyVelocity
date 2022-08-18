package com.github.imdabigboss.easyvelocity;

import com.github.imdabigboss.easyvelocity.commands.*;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.listeners.EventListener;
import com.github.imdabigboss.easyvelocity.managers.*;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION,
        url = PluginInfo.WEBSITE, description = PluginInfo.DESCRIPTION, authors = {PluginInfo.AUTHOR})
public class EasyVelocity {
    private static ProxyServer server = null;
    private static Logger logger = null;

    private static String motdMessage = "O_O No message was set...";

    private static UUIDManager uuidManager = null;
    private static ConfigManager configManager = null;
    private static WhitelistManager whitelistManager = null;
    private static PermissionsManager permissionsManager = null;
    private static PluginMessageManager pluginMessageManager = null;
    private static MaintenanceManager maintenanceManager = null;
    private static RanksManager ranksManager = null;
    private static CustomListManger customListManger = null;
    private static BanManager banManager = null;
    private static NickManager nickManager = null;

    @Inject
    public EasyVelocity(ProxyServer serverIN, Logger loggerIN) {
        server = serverIN;
        logger = loggerIN;

        configManager = new ConfigManager();

        if (configManager.getConfig("config").contains("uuidPath")) {
            String path = configManager.getConfig("config").getString("uuidPath");
            if (!path.isEmpty() && !path.isBlank()) {
                Path uuidPath = Path.of(path).toAbsolutePath();
                configManager.createConfig(uuidPath, "uuid");
                logger.info("Using custom UUID path: " + path);
            }
        }

        this.loadConfigOptions();
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        this.loadManagers();
        this.registerCommands();

        server.getEventManager().register(this, new EventListener());

        customListManger.broadcastCustomList();

        logger.info(PluginInfo.NAME + " " + PluginInfo.VERSION + " is enabled!");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        pluginMessageManager.unregister();
    }

    // --- Util commands ---

    private void loadConfigOptions() {
        PluginConfig config = getConfig();

        if (config.contains("motdMessage")) {
            motdMessage = config.getString("motdMessage");
        }
    }

    private void registerCommands() {
        new BroadcastCommand();
        new InfoCommand();
        new MotdCommand();
        new LobbyCommand();
        new WhitelistCommand();
        new MaintenanceCommand();
        new VelocityKickCommand();
        new VelocityNickCommand();
        new RanksCommand();
        new TempbanCommand();
    }

    private void loadManagers() {
        uuidManager = new UUIDManager();
        whitelistManager = new WhitelistManager();
        permissionsManager = new PermissionsManager();
        pluginMessageManager = new PluginMessageManager();
        maintenanceManager = new MaintenanceManager();
        ranksManager = new RanksManager();
        customListManger = new CustomListManger();
        banManager = new BanManager();
        nickManager = new NickManager();
    }

    // --- Getters & setters ---

    public static ProxyServer getServer() {
        return server;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginConfig getConfig() {
        return getConfig("config");
    }

    public static PluginConfig getConfig(String configName) {
        return configManager.getConfig(configName);
    }

    public static String getMotdMessage() {
        return motdMessage;
    }

    public static void setMotdMessage(String message) {
        motdMessage = message;
    }

    public static UUIDManager getUUIDManager() {
        return uuidManager;
    }

    public static WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public static PermissionsManager getPermissionsManager() {
        return permissionsManager;
    }

    public static PluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
    }

    public static MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }

    public static RanksManager getRanksManager() {
        return ranksManager;
    }

    public static CustomListManger getCustomListManger() {
        return customListManger;
    }

    public static BanManager getBanManager() {
        return banManager;
    }

    public static NickManager getNickManager() {
        return nickManager;
    }
}