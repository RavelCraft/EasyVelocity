/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package com.github.imdabigboss.easyvelocity.utils;

public class ChatColor {
    public static final char ESCAPE = '§';
    public static final String BLACK = ESCAPE + "0";
    public static final String DARK_BLUE = ESCAPE + "1";
    public static final String DARK_GREEN = ESCAPE + "2";
    public static final String DARK_AQUA = ESCAPE + "3";
    public static final String DARK_RED = ESCAPE + "4";
    public static final String DARK_PURPLE = ESCAPE + "5";
    public static final String GOLD = ESCAPE + "6";
    public static final String GRAY = ESCAPE + "7";
    public static final String DARK_GRAY = ESCAPE + "8";
    public static final String BLUE = ESCAPE + "9";
    public static final String GREEN = ESCAPE + "a";
    public static final String AQUA = ESCAPE + "b";
    public static final String RED = ESCAPE + "c";
    public static final String LIGHT_PURPLE = ESCAPE + "d";
    public static final String YELLOW = ESCAPE + "e";
    public static final String WHITE = ESCAPE + "f";
    public static final String OBFUSCATED = ESCAPE + "k";
    public static final String BOLD = ESCAPE + "l";
    public static final String STRIKETHROUGH = ESCAPE + "m";
    public static final String UNDERLINE = ESCAPE + "n";
    public static final String ITALIC = ESCAPE + "o";
    public static final String RESET = ESCAPE + "r";

    public static String stingToChatColor(String string) {
        return switch (string.toUpperCase()) {
            case "AQUA" -> ChatColor.AQUA;
            case "BLACK" -> ChatColor.BLACK;
            case "BLUE" -> ChatColor.BLUE;
            case "DARK_AQUA" -> ChatColor.DARK_AQUA;
            case "DARK_BLUE" -> ChatColor.DARK_BLUE;
            case "DARK_GRAY" -> ChatColor.DARK_GRAY;
            case "DARK_GREEN" -> ChatColor.DARK_GREEN;
            case "DARK_PURPLE" -> ChatColor.DARK_PURPLE;
            case "DARK_RED" -> ChatColor.DARK_RED;
            case "GOLD" -> ChatColor.GOLD;
            case "GRAY" -> ChatColor.GRAY;
            case "GREEN" -> ChatColor.GREEN;
            case "LIGHT_PURPLE" -> ChatColor.LIGHT_PURPLE;
            case "RED" -> ChatColor.RED;
            case "WHITE" -> ChatColor.WHITE;
            case "YELLOW" -> ChatColor.YELLOW;
            default -> ChatColor.RESET;
        };
    }

    public static String chatColorToString(String color) {
        if (ChatColor.AQUA.equals(color)) {
            return "AQUA";
        } else if (ChatColor.BLACK.equals(color)) {
            return "BLACK";
        } else if (ChatColor.BLUE.equals(color)) {
            return "BLUE";
        } else if (ChatColor.DARK_AQUA.equals(color)) {
            return "DARK_AQUA";
        } else if (ChatColor.DARK_BLUE.equals(color)) {
            return "DARK_BLUE";
        } else if (ChatColor.DARK_GRAY.equals(color)) {
            return "DARK_GRAY";
        } else if (ChatColor.DARK_GREEN.equals(color)) {
            return "DARK_GREEN";
        } else if (ChatColor.DARK_PURPLE.equals(color)) {
            return "DARK_PURPLE";
        } else if (ChatColor.DARK_RED.equals(color)) {
            return "DARK_RED";
        } else if (ChatColor.GOLD.equals(color)) {
            return "GOLD";
        } else if (ChatColor.GRAY.equals(color)) {
            return "GRAY";
        } else if (ChatColor.GREEN.equals(color)) {
            return "GREEN";
        } else if (ChatColor.LIGHT_PURPLE.equals(color)) {
            return "LIGHT_PURPLE";
        } else if (ChatColor.RED.equals(color)) {
            return "RED";
        } else if (ChatColor.WHITE.equals(color)) {
            return "WHITE";
        } else if (ChatColor.YELLOW.equals(color)) {
            return "YELLOW";
        } else {
            return "";
        }
    }
}
