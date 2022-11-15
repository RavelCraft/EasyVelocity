package com.github.imdabigboss.easyvelocity.managers;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PluginMessageManager {
    public static final int PLUGIN_MESSAGE_PORT = 17157;

    private ServerSocket serverSocket;
    private boolean listening = true;

    private final List<VelocityServerSocket> serverSockets = new ArrayList<>();

    public PluginMessageManager() {
        try {
            this.serverSocket = new ServerSocket(PLUGIN_MESSAGE_PORT);
        } catch (IOException e) {
            EasyVelocity.getLogger().warn("Unable to start plugin messaging on port " + PLUGIN_MESSAGE_PORT);
            return;
        }

        new Thread(this::listen).start();
    }

    private void listen() {
        while (this.listening) {
            VelocityServerSocket client;
            try {
                Socket socket = this.serverSocket.accept();
                client = new VelocityServerSocket(socket);
            } catch (IOException e) {
                continue;
            }

            if (!this.listening) {
                break;
            }

            this.serverSockets.add(client);
            new Thread(() -> {
                this.clientListen(client);
            }).start();
        }
    }

    private void clientListen(VelocityServerSocket client) {
        DataOutputStream outputStream = client.getOutputStream();
        DataInputStream inputStream = client.getInputStream();

        String serverName = "Not found";

        try {
            //--- Connection handshake ---
            outputStream.writeUTF(ServerInfo.SERVER_NAME + "\n");
            outputStream.flush();

            serverName = inputStream.readUTF();
            RegisteredServer server = EasyVelocity.getServer().getServer(serverName).orElse(null);
            if (server == null) {
                EasyVelocity.getLogger().warn("Unable to find server " + serverName);
                outputStream.writeBoolean(false);
                client.close();
                this.serverSockets.remove(client);
                return;
            }

            client.setServer(server);
            EasyVelocity.getLogger().info("Server connected: " + serverName);

            outputStream.writeBoolean(true);
            outputStream.flush();

            //--- Main loop ---

            while (this.listening) {
                String command = inputStream.readUTF();

                List<String> args = new ArrayList<>();
                while (true) {
                    String line = inputStream.readUTF();
                    if (line.equals("\u0000")) {
                        break;
                    }

                    args.add(line);
                }

                this.runCommand(command, args.toArray(new String[0]));
            }
        } catch (NullPointerException ignored) {
        } catch (IOException e) {
            EasyVelocity.getLogger().warn("Server disconnected: " + serverName);
        }

        client.close();
        this.serverSockets.remove(client);
    }

    public void close() {
        this.listening = false;

        try {
            this.serverSocket.close();
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Unable to close plugin messaging server socket", e);
        }

        for (VelocityServerSocket socket : this.serverSockets) {
            socket.close();
        }
        this.serverSockets.clear();
    }

    private void runCommand(String command, String[] args) throws IOException {
        //Nothing
    }

    public void sendCommand(Player player, String command, String... args) {
        try {
            RegisteredServer server = player.getCurrentServer().get().getServer();
            VelocityServerSocket socket = this.serverSockets.stream().filter(s -> s.getServer().equals(server)).findFirst().orElse(null);
            if (socket == null) {
                EasyVelocity.getLogger().warn("Unable to find socket for server " + server.getServerInfo().getName());
                return;
            }

            DataOutputStream outputStream = socket.getOutputStream();
            outputStream.writeUTF(command);
            for (String arg : args) {
                outputStream.writeUTF(arg);
            }
            outputStream.writeUTF("\u0000");
            outputStream.flush();
        } catch (NullPointerException e) {
            EasyVelocity.getLogger().warn("Unable to find server to send command to");
        } catch (IOException e) {
            EasyVelocity.getLogger().warn("Unable to send command to server");
        }
    }

    private static class VelocityServerSocket {
        private final Socket socket;
        private RegisteredServer server;
        private final DataOutputStream outputStream;
        private final DataInputStream inputStream;

        public VelocityServerSocket(Socket socket) throws IOException {
            this.socket = socket;
            this.server = null;

            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.inputStream = new DataInputStream(socket.getInputStream());
        }

        public RegisteredServer getServer() {
            return this.server;
        }

        public void setServer(RegisteredServer server) {
            this.server = server;
        }

        public DataOutputStream getOutputStream() {
            return this.outputStream;
        }

        public DataInputStream getInputStream() {
            return this.inputStream;
        }

        public void close() {
            try {
                InputStream inputStream = this.socket.getInputStream();
                OutputStream outputStream = this.socket.getOutputStream();
                this.socket.close();

                outputStream.close();
                this.outputStream.close();

                inputStream.close();
                this.inputStream.close();
            } catch (IOException ignored) {
            }
        }
    }
}
