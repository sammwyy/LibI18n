package com.sammwy.libi18n.utils;

import java.lang.reflect.Method;

enum VALUE {
    YES, NO, UNKNOWN
}

public class ServerUtils {
    public static VALUE HAS_PLAYER_GET_LOCALE_METHOD = VALUE.UNKNOWN;
    public static Platform PLATFORM = Platform.UNKNOWN;

    public static Platform getPlatform() {
        if (PLATFORM == Platform.UNKNOWN) {
            if (ReflectionUtils.existClass("org.bukkit.Bukkit")) {
                PLATFORM = Platform.BUKKIT;
            }
        }

        return PLATFORM;
    }

    @SuppressWarnings("deprecation")
    public static boolean hasPlayerGetLocaleAPI() {
        if (HAS_PLAYER_GET_LOCALE_METHOD == VALUE.UNKNOWN) {
            try {
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
                Method method = playerClass.getMethod("getLocale");
                HAS_PLAYER_GET_LOCALE_METHOD = method == null || !method.isAccessible() ? VALUE.NO : VALUE.YES;
            } catch (Exception ignored) {
                HAS_PLAYER_GET_LOCALE_METHOD = VALUE.NO;
            }
        }

        return HAS_PLAYER_GET_LOCALE_METHOD == VALUE.YES;
    }
}