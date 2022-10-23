package com.github.imdabigboss.easyvelocity.utils;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import net.kyori.adventure.text.Component;
import org.geysermc.floodgate.api.FloodgateApi;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class TexturePack {
    private static Path pathToExportZip = null;
    private static String packURL = null;
    private static int packVersion = 0;

    private static Path outputPackPath;
    private static Path sourcePackPath;
    private static Path tmpPackPath;

    public static void init() {
        if (!EasyVelocity.getConfig().contains("packURL")) {
            EasyVelocity.getLogger().error("You need to set an export path for the texture pack.");
            return;
        }
        if (!EasyVelocity.getConfig().contains("pathToExportZip")) {
            EasyVelocity.getLogger().error("You need to set a webserver path to host the pack on.");
            return;
        }

        packURL = EasyVelocity.getConfig().getString("packURL");
        pathToExportZip = Path.of(EasyVelocity.getConfig().getString("pathToExportZip"));
        if (EasyVelocity.getConfig().contains("packVersion")) {
            packVersion = EasyVelocity.getConfig().getInt("packVersion");
        }

        outputPackPath = Paths.get("plugins/" + PluginInfo.NAME + "/pack.zip");
        sourcePackPath = Paths.get("plugins/" + PluginInfo.NAME + "/pack");
        tmpPackPath = Paths.get("plugins/" + PluginInfo.NAME + "/tmp");
    }

    public static boolean generatePack() {
        packVersion += 1;
        EasyVelocity.getConfig().set("packVersion", packVersion);
        EasyVelocity.getConfig().save();
        EasyVelocity.getLogger().info("Generating texture pack v" + packVersion + "...");

        createIfNotExists(sourcePackPath);
        createIfNotExists(sourcePackPath.resolve("assets"));
        createIfNotExists(sourcePackPath.resolve("assets/minecraft"));

        createIfNotExists(sourcePackPath.resolve("assets/minecraft/textures"));
        createIfNotExists(sourcePackPath.resolve("assets/minecraft/textures/item"));
        createIfNotExists(sourcePackPath.resolve("assets/minecraft/textures/material"));

        createIfNotExists(sourcePackPath.resolve("assets/minecraft/models"));
        createIfNotExists(sourcePackPath.resolve("assets/minecraft/models/item"));

        copyOutOfJarIfNotExists("pack.png", sourcePackPath.resolve("pack.png"));
        copyOutOfJarIfNotExists("pack.mcmeta", sourcePackPath.resolve("pack.mcmeta"));
        copyOutOfJarIfNotExists("LICENSE", sourcePackPath.resolve("LICENSE"));

        File zip = outputPackPath.toFile();
        if (zip.exists()) {
            zip.delete();
        }

        try {
            deleteTempPackFolder();

            createIfNotExists(tmpPackPath);
            copyPackFile(sourcePackPath.resolve("pack.png"), tmpPackPath.resolve("pack.png"));
            copyPackFile(sourcePackPath.resolve("pack.mcmeta"), tmpPackPath.resolve("pack.mcmeta"));
            copyPackFile(sourcePackPath.resolve("LICENSE"), tmpPackPath.resolve("LICENSE"));
            copyPackFile(sourcePackPath.resolve("assets"), tmpPackPath.resolve("assets"));

            ZipUtil.pack(tmpPackPath.toFile(), zip);
        } catch (Exception exception) {
            EasyVelocity.getLogger().error("Unable to export zip file", exception);
            if (zip.exists()) {
                zip.delete();
            }
            deleteTempPackFolder();
            return false;
        }

        if (!zip.exists()) {
            EasyVelocity.getLogger().info("Unable to export zip file.");
        }

        try {
            Files.move(outputPackPath, pathToExportZip, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Unable to export zip file", e);
            return false;
        }

        deleteTempPackFolder();
        return true;
    }

    private static void createIfNotExists(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                EasyVelocity.getLogger().error("Unable to create directory", e);
            }
        }
    }

    private static void copyOutOfJarIfNotExists(String jarPath, Path out) {
        if (!Files.exists(out)) {
            try {
                InputStream inputStream = EasyVelocity.class.getClassLoader().getResourceAsStream(jarPath);
                FileOutputStream outputStream = new FileOutputStream(out.toFile());

                byte[] buf = new byte[1024];
                int i;
                while ((i = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, i);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void copyPackFile(Path path, Path outpath) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        Files.createDirectories(outpath.getParent());
        if (Files.isDirectory(path)) {
            FileUtils.copyFolder(path, outpath);
        } else {
            Files.copy(path, outpath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void deleteTempPackFolder() {
        try {
            if (Files.exists(tmpPackPath)) {
                FileUtils.deleteFolder(tmpPackPath);
            }
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Unable to delete tmp pack folder", e);
        }
    }

    public static boolean sendTexturePackToPlayer(Player player) {
        if (EasyVelocity.isFloodgateAPI()) {
            if (FloodgateApi.getInstance().isFloodgateId(player.getUniqueId())) {
                player.sendMessage(Component.text(ChatColor.YELLOW + "You are playing on Minecraft Bedrock edition. We are warning you that this server uses a texture pack that is not supported by Bedrock at this point."));
                return false;
            }
        }

        ResourcePackInfo packInfo = EasyVelocity.getServer().createResourcePackBuilder(packURL + "?v=" + packVersion)
                .setPrompt(Component.text("RavelCraft uses a texture pack to add some stuff. We recommend that you use it by clicking \"Yes\"."))
                .setShouldForce(false)
                .build();

        player.sendResourcePackOffer(packInfo);
        EasyVelocity.getLogger().info("Sent texture pack v" + packVersion + " to: " + player.getUsername());
        return true;
    }
}
