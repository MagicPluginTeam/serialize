package io.github.magicpluginteam.serialize.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorUtils {

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static Color getRGB(int hex) {
        return Color.fromRGB(hex);
    }

    public static Color hex2RGB(String string) {
        if (string == null) {
            throw new AssertionError("string is null");
        }
        if (string.startsWith("#"))
            string = string.substring(1);
        return Color.fromRGB(
                Integer.parseInt(string.substring(0, 2)),
                Integer.parseInt(string.substring(2, 4)),
                Integer.parseInt(string.substring(4, 6))
        );
    }

}
