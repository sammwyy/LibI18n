package com.sammwy.libi18n.plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sammwy.libi18n.LanguageManager;

public class Li18nBukkitPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        LanguageManager langs = LanguageManager.forBukkit(this, "lang")
                .withDefault("en")
                .extractIfEmpty("lang/");
        langs.tryLoadLanguages();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : this.getServer().getOnlinePlayers()) {
                String message = langs.getLanguage(player).get("dummy");
                player.sendMessage(message);
            }
        }, 0L, 20L);
    }
}