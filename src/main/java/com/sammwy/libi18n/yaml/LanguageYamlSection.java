package com.sammwy.libi18n.yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageYamlSection {
    protected Map<String, Object> values;

    public LanguageYamlSection() {
    }

    public LanguageYamlSection(Map<String, Object> values) {
        this.values = values;
    }

    // Getters
    @SuppressWarnings("unchecked")
    public LanguageYamlSection getSection(String key) {
        Map<String, Object> child = (Map<String, Object>) this.get(key);
        return new LanguageYamlSection(child);
    }

    public Object get(String key) {
        LanguageYamlSection section = this;
        String[] paths = key.split("[.]");

        for (int i = 1; i < paths.length; i++) {
            section = section.getSection(paths[i - 1]);
            key = paths[i];
        }

        return section == null ? null : section.values.get(key);
    }

    public Set<String> getKeys() {
        return this.values.keySet();
    }

    public String getString(String key) {
        Object value = this.get(key);
        return value == null ? null : (String) value;
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key) {
        Object value = this.get(key);
        return value == null ? null : (List<String>) value;
    }

    // Utils
    public Map<String, Object> toMap() {
        return this.values;
    }
}