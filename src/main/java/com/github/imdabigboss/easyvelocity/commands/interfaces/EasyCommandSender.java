package com.github.imdabigboss.easyvelocity.commands.interfaces;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

public class EasyCommandSender {
    private final CommandSource source;

    public EasyCommandSender(CommandSource source) {
        this.source = source;
    }

    public CommandSource getSource() {
        return source;
    }

    public boolean hasPermission(String permission) {
        return this.source.hasPermission(permission);
    }

    public void sendMessage(@NotNull String message) {
        this.source.sendMessage(Component.text(message));
    }

    public void sendActionBar(@NotNull String message) {
        this.source.sendActionBar(Component.text(message));
    }

    public void sendPlayerListHeader(@NotNull String header) {
        this.source.sendPlayerListHeader(Component.text(header));
    }

    public void sendPlayerListFooter(@NotNull String footer) {
        this.source.sendPlayerListFooter(Component.text(footer));
    }

    public void sendPlayerListHeaderAndFooter(@NotNull String header, @NotNull String footer) {
        this.source.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
    }

    public void showTitle(@NotNull Title title) {
        this.source.showTitle(title);
    }

    public void clearTitle() {
        this.source.clearTitle();
    }

    public void resetTitle() {
        this.source.resetTitle();
    }

    public void showBossBar(@NotNull BossBar bar) {
        this.source.showBossBar(bar);
    }

    public void hideBossBar(@NotNull BossBar bar) {
        this.source.hideBossBar(bar);
    }

    public void playSound(@NotNull Sound sound) {
        this.source.playSound(sound);
    }

    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        this.source.playSound(sound, x, y, z);
    }

    public void stopSound(@NotNull Sound sound) {
        this.source.stopSound(sound);
    }

    public void playSound(@NotNull Sound sound, Sound.@NotNull Emitter emitter) {
        this.source.playSound(sound, emitter);
    }

    public void stopSound(@NotNull SoundStop stop) {
        this.source.stopSound(stop);
    }

    public void openBook(Book.@NotNull Builder book) {
        this.source.openBook(book);
    }

    public void openBook(@NotNull Book book) {
        this.source.openBook(book);
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
