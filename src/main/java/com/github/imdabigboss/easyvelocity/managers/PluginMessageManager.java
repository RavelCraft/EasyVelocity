package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.*;
import java.util.Optional;

public class PluginMessageManager {
    private static final ChannelIdentifier CHANNEL_ID = MinecraftChannelIdentifier.create("imdabigboss", "main");

    public PluginMessageManager() {
        EasyVelocity.getServer().getChannelRegistrar().register(CHANNEL_ID);
    }

    public void unregister() {
        EasyVelocity.getServer().getChannelRegistrar().unregister(CHANNEL_ID);
    }

    public void sendPlayerCommand(Player player, String command, String data, String channel) {
        Optional<ServerConnection> server = player.getCurrentServer();
        if (server.isPresent()) {
            this.sendPlayerCommand(server.get().getServer(), player, command, data, channel);
        } else {
            EasyVelocity.getLogger().error("Failed to send a message to " + player.getUsername() + ", they are not connected to a server!");
        }
    }

    public void sendPlayerCommand(RegisteredServer server, Player player, String command, String data, String channel) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(command);
            out.writeUTF(data);
            out.writeUTF(player.getUniqueId().toString());
        } catch (IOException e) {
            return;
        }

        server.sendPluginMessage(CHANNEL_ID, b.toByteArray());
        //EasyVelocity.getLogger().info("Sent plugin message to " + player.getUsername() + ": " + command + " " + data);

        try {
            out.close();
            b.close();
        } catch (IOException ignored) {
        }
    }
}
