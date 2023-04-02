package com.sammwy.libi18n;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sammwy.libi18n.errors.LanguageLoadException;
import com.sammwy.libi18n.utils.FileUtils;
import com.sammwy.libi18n.utils.PlayerUtils;

public class LanguageManager {
    private File directory;
    private File jarFile;

    private String defaultLanguageCode;
    private Map<String, Language> languages;

    protected LanguageManager(File directory, File jarFile) {
        this.directory = directory;
        this.jarFile = jarFile;

        this.defaultLanguageCode = null;
        this.languages = new HashMap<>();
    }

    public LanguageManager extract(String languagePackage) {
        try (JarFile jar = new JarFile(this.jarFile)) {
            for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(languagePackage) && name.endsWith(".yml")) {
                    InputStream stream = jar.getInputStream(entry);
                    File target = new File(this.directory, new File(name).getName());
                    Files.copy(stream, target.getAbsoluteFile().toPath());
                    stream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public LanguageManager extractIfEmpty(String languagePackage) {
        if (this.directory.exists()) {
            if (this.directory.listFiles().length > 0) {
                return this;
            }
        } else {
            this.directory.mkdirs();
        }

        return this.extract(languagePackage);
    }

    public Language getDefaultLanguage() {
        if (this.defaultLanguageCode != null) {
            return this.languages.get(this.defaultLanguageCode);
        } else {
            return null;
        }
    }

    public Language getLanguage(String code) {
        if (code != null && !code.isEmpty()) {
            code = code.toLowerCase();

            if (this.languages.containsKey(code)) {
                return this.languages.get(code);
            } else if (code.contains("_")) {
                String genericLang = code.split("[_]")[0];

                if (this.languages.containsKey(genericLang)) {
                    return this.languages.get(genericLang);
                }
            }
        }

        return this.getDefaultLanguage();
    }

    public Language getLanguage(Player player) {
        return this.getLanguage(PlayerUtils.getPlayerLocale(player));
    }

    public void loadLanguage(File file) throws LanguageLoadException {
        String key = FileUtils.getBaseName(file);
        Language language = new Language(file, this);
        language.load();
        this.registerLanguage(key, language);
    }

    public void loadLanguages() throws LanguageLoadException {
        for (File child : this.directory.listFiles()) {
            if (child.getName().endsWith(".yml")) {
                this.loadLanguage(child);
            }
        }
    }

    public boolean tryLoadLanguages() {
        try {
            this.loadLanguages();
            return true;
        } catch (LanguageLoadException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerLanguage(String code, Language language) {
        this.languages.put(code.toLowerCase(), language);
    }

    public LanguageManager withDefault(String defaultLanguage) {
        this.defaultLanguageCode = defaultLanguage;
        return this;
    }

    public static LanguageManager forBukkit(JavaPlugin plugin, File languageDir) {
        try {
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File jarFile = (File) getFileMethod.invoke(plugin);

            return new LanguageManager(languageDir, jarFile);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LanguageManager forBukkit(JavaPlugin plugin, String languageDir) {
        return forBukkit(plugin, new File(plugin.getDataFolder(), languageDir));
    }
}
