package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

public class PluginMessageManager {
    public static final ChannelIdentifier CHANNEL_ID = MinecraftChannelIdentifier.create("imdabigboss", "main");

    public PluginMessageManager() {
        EasyVelocity.getServer().getChannelRegistrar().register(CHANNEL_ID);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        //EasyVelocity.getLogger().info("Plugin Message Received " + event.getIdentifier());

        if (event.getIdentifier().getId().equals(PluginMessageManager.CHANNEL_ID.getId())) {
            event.setResult(PluginMessageEvent.ForwardResult.handled());

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String channel = in.readUTF();
                if (channel.equals("EasyCMD")) {
                    String cmd = in.readUTF();
                    String data = in.readUTF();
                    this.runPluginCommand(cmd, data);
                }
            } catch (IOException ignored) {}
        }
    }

    private void runPluginCommand(String command, String data) {
        if (command.equalsIgnoreCase("playerjoined")) {
            EasyVelocity.getCustomListManger().broadcastCustomList();

            UUID player = UUID.fromString(data);
            EasyVelocity.getRanksManager().displayRank(player);
        }
    }

    public void sendPlayerCommand(Player player, String command, String data, String channel) {
        Optional<ServerConnection> server = player.getCurrentServer();
        if (server.isPresent()) {
            this.sendPlayerCommand(server.get().getServer(), command, data, channel);
        } else {
            EasyVelocity.getLogger().error("Failed to send a message to " + player.getUsername() + ", they are not connected to a server!");
        }
    }

    public void sendPlayerCommand(RegisteredServer server, String command, String data, String channel) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(command);
            out.writeUTF(data);
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
