package com.sammwy.libi18n.utils;

import java.lang.reflect.Method;

enum VALUE {
    YES, NO, UNKNOWN
}

public class ServerUtils {
    private static VALUE HAS_PLAYER_GET_LOCALE_METHOD = VALUE.UNKNOWN;
    private static Platform PLATFORM = Platform.UNKNOWN;

    public static Platform getPlatform() {
        if (PLATFORM == Platform.UNKNOWN) {
            if (ReflectionUtils.existClass("org.bukkit.Bukkit")) {
                PLATFORM = Platform.BUKKIT;
            }
        }

        return PLATFORM;
    }

    public static boolean hasPlayerGetLocaleAPI() {
        System.out.println("Patata");
        if (HAS_PLAYER_GET_LOCALE_METHOD == VALUE.UNKNOWN) {
            System.out.println("Chocolate");
            try {
                Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
                System.out.println("Torta");
                Method method = playerClass.getMethod("getLocale");
                System.out.println("Budin");
                HAS_PLAYER_GET_LOCALE_METHOD = method == null ? VALUE.NO : VALUE.YES;
                System.out.println("Biscochuelo");
            } catch (Exception ignored) {
                System.out.println("Salchicha");
                HAS_PLAYER_GET_LOCALE_METHOD = VALUE.NO;
            }
        }

        System.out.println("BRUH BURH BURH " + HAS_PLAYER_GET_LOCALE_METHOD);
        return HAS_PLAYER_GET_LOCALE_METHOD == VALUE.YES;
    }
}