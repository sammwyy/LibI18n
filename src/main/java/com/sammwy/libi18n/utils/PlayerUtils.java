package com.sammwy.libi18n.utils;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

public class PlayerUtils {
    public static String getPlayerLocaleInLegacyWay(Player player) {
        try {
            Object ep = ReflectionUtils.getMethod("getHandle", player.getClass()).invoke(player, (Object[]) null);
            Field f = ep.getClass().getDeclaredField("locale");
            f.setAccessible(true);
            return (String) f.get(ep);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPlayerLocale(Player player) {
        if (ServerUtils.hasPlayerGetLocaleAPI()) {
            return player.getLocale();
        } else {
            return getPlayerLocaleInLegacyWay(player);
        }
    }
}
