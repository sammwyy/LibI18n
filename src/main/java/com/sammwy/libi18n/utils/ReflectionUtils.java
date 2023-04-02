package com.sammwy.libi18n.utils;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class ReflectionUtils {
    public static String PACKAGE = null;

    static {
        if (ServerUtils.getPlatform() == Platform.BUKKIT) {
            PACKAGE = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
    }

    public static boolean existClass(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static Class<?> getCBClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + PACKAGE + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + PACKAGE + "." + name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Method getMethod(String name, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name))
                return m;
        }

        return null;
    }
}
