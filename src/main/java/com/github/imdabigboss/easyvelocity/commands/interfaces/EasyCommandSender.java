package com.github.imdabigboss.easyvelocity.commands.interfaces;

import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class EasyCommandSender {
    private final CommandSource source;

    public EasyCommandSender(CommandSource source) {
        this.source = source;
    }

    public void sendMessage(@NotNull PlayerMessage message, String... args) {
        this.source.sendMessage(PlayerMessage.formatMessage(message, this, args));
    }

    public void sendMessage(@NotNull String message) {
        this.source.sendMessage(Component.text(message));
    }

    public boolean isConsole() {
        return this.source instanceof ConsoleCommandSource;
    }

    public Player getPlayer() {
        if (this.source instanceof Player) {
            return (Player) this.source;
        }
        return null;
    }
}
