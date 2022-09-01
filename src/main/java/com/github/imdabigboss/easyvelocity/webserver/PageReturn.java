package com.github.imdabigboss.easyvelocity.webserver;

public class PageReturn {
    private final byte[] data;
    private final int statusCode;
    private final String mime;

    public PageReturn(byte[] data, int statusCode, String mime) {
        this.data = data;
        this.statusCode = statusCode;
        this.mime = mime;
    }

    public PageReturn(String data, int statusCode, String mime) {
        this(data.getBytes(), statusCode, mime);
    }

    public PageReturn(byte[] data, String mime) {
        this(data, 200, mime);
    }

    public PageReturn(String data, int statusCode) {
        this(data, statusCode, "text/html");
    }

    public PageReturn(String data, String mime) {
        this(data, 200, mime);
    }

    public PageReturn(String data) {
        this(data, 200);
    }

    public byte[] getData() {
        if (this.data == null) {
            return new byte[0];
        }
        return this.data;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getMime() {
        return this.mime;
    }
}
