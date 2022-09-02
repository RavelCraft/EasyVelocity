package com.github.imdabigboss.easyvelocity.webserver;

import java.util.Locale;

public enum MimeType {
    PLAIN("text/plain"),

    HTML("text/html"),
    JS("application/javascript"),
    CSS("text/css"),
    PNG("image/png"),
    MAP("application/json"),
    ICO("image/x-icon"),
    ZIP("application/zip"),
    JAR("application/java-archive"),

    EOT("application/vnd.ms-fontobject"),
    SVG("image/svg+xml"),
    TTF("font/ttf"),
    WOFF("font/woff"),
    WOFF2("font/woff2");

    private final String mimeType;

    MimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getExtension() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static MimeType getMimeType(String extension) {
        for (MimeType mimeType : MimeType.values()) {
            if (mimeType.getExtension().equals(extension)) {
                return mimeType;
            }
        }
        return null;
    }
}

