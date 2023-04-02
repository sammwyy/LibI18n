package com.sammwy.libi18n;

import java.io.File;

import com.sammwy.libi18n.errors.LanguageLoadException;
import com.sammwy.libi18n.yaml.LanguageYamlFile;

public class Language {
    private LanguageYamlFile file;
    private LanguageManager manager;

    public Language(File file, LanguageManager manager) {
        this.file = new LanguageYamlFile(file);
        this.manager = manager;
    }

    public void load() throws LanguageLoadException {
        this.file.load();
    }

    public String get(String key) {
        Language fallback = this.manager.getDefaultLanguage();
        String value = this.file.getString(key);

        if (value != null) {
            return value;
        } else if (fallback != null && fallback != this) {
            return fallback.get(key);
        } else {
            return "<Missing key '" + key + "'>";
        }
    }
}
