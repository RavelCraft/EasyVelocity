package com.github.imdabigboss.easyvelocity.listeners;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.github.imdabigboss.easyvelocity.managers.MaintenanceManager;
import com.github.imdabigboss.easyvelocity.managers.PluginMessageManager;
import com.github.imdabigboss.easyvelocity.utils.ChatColor;
import com.github.imdabigboss.easyvelocity.utils.PluginConfig;
import com.github.imdabigboss.easyvelocity.utils.ServerUtils;
import com.github.imdabigboss.easyvelocity.utils.Utils;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.*;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import com.velocitypowered.api.util.GameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.*;

public class EventListener {
    private final List<String> justJoined = new ArrayList<>();
    private final List<String> loulouServer =	new ArrayList<>();

    private final ServerPing.SamplePlayer[] samplePlayers;
    private final Favicon emptyFavicon;
    private Favicon serverFavicon;

    public EventListener() {
        UUID emptyUUID = new UUID(0, 0);
        emptyFavicon = Favicon.create(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));

        try {
            serverFavicon = Favicon.create(Paths.get("server-icon.png"));
        } catch (IOException e) {
            EasyVelocity.getLogger().warn("Failed to load server icon.", e);
            serverFavicon = emptyFavicon;
        }

        samplePlayers = new ServerPing.SamplePlayer[5];
        samplePlayers[0] = new ServerPing.SamplePlayer(ChatColor.GREEN + "---------- Ravel Craft Network ----------", emptyUUID);
        samplePlayers[1] = new ServerPing.SamplePlayer(ChatColor.YELLOW + "          A friendly community server!", emptyUUID);
        samplePlayers[2] = new ServerPing.SamplePlayer("", emptyUUID);
        samplePlayers[3] = new ServerPing.SamplePlayer(ChatColor.YELLOW + "            Maintained with " + ChatColor.RED + "❤" + ChatColor.YELLOW + " by Alex!", emptyUUID);
        samplePlayers[4] = new ServerPing.SamplePlayer(ChatColor.GREEN + "---------------------------------------", emptyUUID);
    }

    @Subscribe
    public void onPermissionSetupEvent(PermissionsSetupEvent event) {
        event.setProvider(subject -> permission -> {
            if (!(subject instanceof CommandSource)) {
                return Tristate.fromBoolean(false);
            }

            CommandSource source = (CommandSource) subject;
            boolean res = EasyVelocity.getPermissionsManager().hasPermission(source, permission);
            return Tristate.fromBoolean(res);
        });
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPreLoginEvent(PreLoginEvent event) {
        String serverIP = ServerInfo.RAVELCRAFT_IP;
        Optional<InetSocketAddress> connect = event.getConnection().getVirtualHost();
        if (connect.isPresent()) {
            InetSocketAddress address = connect.get();
            if (address.getHostString().equals(ServerInfo.RAVELCRAFT_IP)) {
                serverIP = ServerInfo.RAVELCRAFT_IP;
            } else if (address.getHostString().equals(ServerInfo.LOULOU_IP)) {
                loulouServer.add(event.getUsername());
                serverIP = ServerInfo.LOULOU_IP;
            } else if (address.getHostString().equals("localhost")) {
                serverIP = ServerInfo.RAVELCRAFT_IP;
            } else {
                serverIP = address.getHostString();
            }
        }

        if (!serverIP.equals(ServerInfo.RAVELCRAFT_IP) && !serverIP.equals(ServerInfo.LOULOU_IP)) {
            event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text("This server is private, if you think you should be able to join then you know how to contact the server owner.")));
            return;
        }

        justJoined.add(event.getUsername());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLoginEvent(LoginEvent event) {
        UUID originalPlayerUUID = EasyVelocity.getNickManager().getOriginalPlayer(event.getPlayer().getUniqueId());
        if (originalPlayerUUID == null) {
            if (EasyVelocity.getMaintenanceManager().isMaintenance()) {
                if (!EasyVelocity.getMaintenanceManager().canPlayerBypass(event.getPlayer().getUniqueId())) {
                    event.setResult(ResultedEvent.ComponentResult.denied(MaintenanceManager.getMaintenanceMessage(event.getPlayer().getUsername())));
                }
            }

            if (!EasyVelocity.getWhitelistManager().isWhitelisted(event.getPlayer().getUniqueId())) {
                event.setResult(ResultedEvent.ComponentResult.denied(Component.text(ChatColor.RED + "We are very sorry " + event.getPlayer().getUsername() + ", but you are not whitelisted on this server!")));
            }

            String banMessage = EasyVelocity.getBanManager().getPlayerBanMessage(event.getPlayer().getUniqueId());
            if (banMessage != null) {
                event.setResult(ResultedEvent.ComponentResult.denied(Component.text(banMessage)));
            }
        } else {
            EasyVelocity.getLogger().info("Player " + event.getPlayer().getUsername() + " has logged in as a nick from " + EasyVelocity.getUUIDManager().getPlayerName(originalPlayerUUID) + ".");
            event.getPlayer().sendMessage(Component.text(ChatColor.RED + "You have logged in as a nick! Beware!"));
        }

        new Thread(() -> {
            EasyVelocity.getUUIDManager().registerUUID(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());
        }).start();

        ServerUtils.broadcast(ChatColor.YELLOW + event.getPlayer().getUsername() + " has joined the network!");
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onServerPreConnectEvent(ServerPreConnectEvent event) {
        String playerName = event.getPlayer().getUsername();

        if (loulouServer.contains(playerName)) {
            loulouServer.remove(playerName);

            Optional<RegisteredServer> loulou = EasyVelocity.getServer().getServer(ServerInfo.LOULOU_SERVER_NAME);
            if (loulou.isPresent()) {
                event.setResult(ServerPreConnectEvent.ServerResult.allowed(loulou.get()));
            }
        }

        if (!justJoined.contains(playerName)) {
            event.getResult().getServer().ifPresent(server -> {
                Player player = event.getPlayer();
                String serverName = server.getServerInfo().getName();
                EasyVelocity.getLogger().info(playerName + " is connecting to " + serverName);

                if (!serverName.equals(ServerInfo.LOBBY_SERVER_NAME) && !EasyVelocity.getWhitelistManager().isWhitelisted(player.getUniqueId(), serverName)) {
                    event.setResult(ServerPreConnectEvent.ServerResult.denied());
                    player.sendMessage(Component.text(ChatColor.RED + "We are very sorry " + playerName + ", but you are not whitelisted on this server!"));
                }
            });
        }
    }

    @Subscribe
    public void onServerPostConnectEvent(ServerPostConnectEvent event) {
        Player player = event.getPlayer();

        Optional<ServerConnection> optionalServerConnection = player.getCurrentServer();
        if (optionalServerConnection.isEmpty()) {
            return;
        }
        RegisteredServer server = optionalServerConnection.get().getServer();

        if (justJoined.contains(player.getUsername())) {
            justJoined.remove(player.getUsername());

            EasyVelocity.getPluginMessageManager().sendPlayerCommand(server, player, "sendresourcepack", "", "RavelDatapack");

            int out = Utils.getRandomNumberInRange(1, 4);
            if (out == 1) {
                event.getPlayer().sendMessage(Component.text(ChatColor.AQUA + "Hey there " + player.getUsername() + "! Did you read the rules??? Click this message to do so! " + PluginInfo.WEBSITE + "/rules").clickEvent(ClickEvent.openUrl(PluginInfo.WEBSITE + "/rules")));
            }
        }

        EasyVelocity.getRanksManager().displayRank(player.getUniqueId());
        EasyVelocity.getCustomListManger().broadcastCustomList();
    }

    @Subscribe
    public void onDisconnectEvent(DisconnectEvent event) {
        ServerUtils.broadcast(ChatColor.YELLOW + event.getPlayer().getUsername() + " has left the network!");

        EasyVelocity.getCustomListManger().broadcastCustomList();
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerKickEvent(KickedFromServerEvent event) {
        if (!event.getServer().getServerInfo().getName().equals(ServerInfo.LOBBY_SERVER_NAME)) {
            Optional<RegisteredServer> lobby = EasyVelocity.getServer().getServer(ServerInfo.LOBBY_SERVER_NAME);
            if (lobby.isPresent()) {
                event.getPlayer().sendMessage(Component.text(ChatColor.RED + "You are now being redirected to the lobby."));
                EasyVelocity.getLogger().info("Redirecting " + event.getPlayer().getUsername() + " to lobby, because of kick.");

                event.setResult(KickedFromServerEvent.RedirectPlayer.create(lobby.get()));
            }
        }
    }

    @Subscribe
    public void onGameProfileRequest(GameProfileRequestEvent event) {
        if (event.isOnlineMode()) {
            UUID oldUuid = event.getGameProfile().getId();
            if (oldUuid == null) {
                return;
            }

            UUID newUuid = EasyVelocity.getNickManager().getNick(oldUuid);
            if (newUuid != null) {
                event.setGameProfile(new GameProfile(newUuid, EasyVelocity.getUUIDManager().getPlayerName(newUuid), Collections.singletonList(new GameProfile.Property("textures", "", ""))));
            }
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onProxyPingEvent(ProxyPingEvent event) {
        String host = ServerInfo.RAVELCRAFT_IP;
        if (event.getConnection().getVirtualHost().isPresent()) {
            host = event.getConnection().getVirtualHost().get().getHostString();
        }

        if (host.equals("localhost")) {
            ServerPing.Builder serverPing = ServerPing.builder()
                    .onlinePlayers(EasyVelocity.getServer().getPlayerCount())
                    .maximumPlayers(ServerInfo.MAX_PLAYERS)
                    .description(Component.text(ChatColor.AQUA + "TEST SERVER"))
                    .version(new ServerPing.Version(event.getPing().getVersion().getProtocol(), ChatColor.RED + "Use versions 1.8.9 to 1.18.x+"))
                    .favicon(emptyFavicon)
                    .samplePlayers(samplePlayers);

            event.setPing(serverPing.build());

            return;
        }

        boolean isRavelCraft = host.equals(ServerInfo.RAVELCRAFT_IP);
        boolean isLoulou = host.equals(ServerInfo.LOULOU_IP);

        if (!isRavelCraft && !isLoulou) {
            ServerPing.Builder serverPing = ServerPing.builder()
                    .onlinePlayers(0)
                    .maximumPlayers(0)
                    .description(Component.text("Something went wrong, you may be unable to join the server..."))
                    .favicon(emptyFavicon)
                    .version(new ServerPing.Version(0, ChatColor.RED + "You may be unable to connect"));

            event.setPing(serverPing.build());
            return;
        }

        if (EasyVelocity.getMaintenanceManager().isMaintenance()) {
            ServerPing.Builder serverPing = ServerPing.builder()
                    .onlinePlayers(EasyVelocity.getServer().getPlayerCount())
                    .maximumPlayers(ServerInfo.MAX_PLAYERS)
                    .samplePlayers(samplePlayers)
                    .version(new ServerPing.Version(0, ChatColor.RED + "You must be OP to join!"))
                    .description(Component.text(ChatColor.AQUA + "The server is currently under maintenance..."));

            event.setPing(serverPing.build());
            return;
        }

        ServerPing.Builder serverPing = ServerPing.builder()
                .version(new ServerPing.Version(event.getPing().getVersion().getProtocol(), ChatColor.RED + "Use versions 1.8.9 to 1.18.x+"))
                .favicon(serverFavicon)
                .samplePlayers(samplePlayers);

        if (isRavelCraft) {
            serverPing.onlinePlayers(EasyVelocity.getServer().getPlayerCount())
                    .maximumPlayers(ServerInfo.MAX_PLAYERS)
                    .description(Component.text(ChatColor.GREEN + "Welcome to the " + ChatColor.RED + ServerInfo.SERVER_NAME + "\n" + ChatColor.YELLOW + EasyVelocity.getMotdMessage()));
        } else {
            serverPing.onlinePlayers(EasyVelocity.getServer().getPlayerCount())
                    .maximumPlayers(ServerInfo.MAX_LOULOU_PLAYERS)
                    .description(Component.text(ChatColor.GREEN + ServerInfo.SERVER_NAME + ChatColor.RED + " Loulou server!\n" + ChatColor.YELLOW + EasyVelocity.getMotdMessage()));
        }

        event.setPing(serverPing.build());
    }
}
