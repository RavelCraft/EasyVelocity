package com.github.imdabigboss.easyvelocity.utils;

import com.github.imdabigboss.easyvelocity.EasyVelocity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {
    public static void deleteFolder(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void copyFolder(Path source, Path destination) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(destination.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, destination.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static String resourceToString(String resource) {
        InputStream stream = EasyVelocity.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            EasyVelocity.getLogger().error("Cannot get resource \"" + resource + "\" from jar file.");
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Error reading resource \"" + resource + "\" from jar file.");
            return "";
        }

        return sb.toString();
    }

    public static byte[] resourceToBytes(String resource) {
        InputStream stream = EasyVelocity.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            EasyVelocity.getLogger().error("Cannot get resource \"" + resource + "\" from jar file.");
            return new byte[0];
        }

        try {
            return stream.readAllBytes();
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Error reading resource \"" + resource + "\" from jar file.");
            return new byte[0];
        }
    }

}
