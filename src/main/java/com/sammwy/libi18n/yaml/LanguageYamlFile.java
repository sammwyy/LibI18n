package com.sammwy.libi18n.yaml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.UnicodeReader;

import com.sammwy.libi18n.errors.LanguageLoadException;

public class LanguageYamlFile extends LanguageYamlSection {
    private File file;
    private String raw;

    private final DumperOptions yamlDumperOptions;
    private final LoaderOptions yamlLoaderOptions;
    private final YamlConstructor constructor;
    private final YamlRepresenter representer;
    private final Yaml yaml;

    public LanguageYamlFile(String raw) {
        this.raw = raw != null && raw.isEmpty() ? null : raw;

        constructor = new YamlConstructor();
        representer = new YamlRepresenter();
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yamlDumperOptions = new DumperOptions();
        yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlLoaderOptions = new LoaderOptions();
        yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
        yamlLoaderOptions.setCodePointLimit(Integer.MAX_VALUE);

        yaml = new Yaml(constructor, representer, yamlDumperOptions, yamlLoaderOptions);
    }

    public LanguageYamlFile(File file) {
        this("");
        this.file = file;
    }

    // Utils
    public Map<String, Object> toMap() {
        return this.values;
    }

    public String toString() {
        return this.yaml.dump(this.values);
    }

    // Load/save
    public String loadAsString() throws LanguageLoadException {
        if (this.raw != null) {
            return raw;
        } else {
            try {
                return Files.readString(this.file.toPath());
            } catch (IOException e) {
                throw new LanguageLoadException(e);
            }
        }
    }

    public void load() throws LanguageLoadException {
        String raw = this.loadAsString();

        try (Reader reader = new UnicodeReader(new ByteArrayInputStream(raw.getBytes(StandardCharsets.UTF_8)))) {
            this.values = yaml.load(reader);
        } catch (YAMLException | IOException e) {
            throw new LanguageLoadException(e);
        } catch (ClassCastException e) {
            throw new LanguageLoadException(e);
        }
    }

    public void save() throws IOException {
        String raw = this.toString();
        Files.writeString(this.file.toPath(), raw, StandardCharsets.UTF_8);
    }

    public void tryLoad() {
        try {
            this.load();
        } catch (LanguageLoadException e) {
            e.printStackTrace();
        }
    }

    public void trySave() {
        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}