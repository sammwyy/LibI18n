package com.sammwy.libi18n.utils;

import java.io.File;

public class FileUtils {
    public static String getBaseName(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    public static String getBaseName(File file) {
        return getBaseName(file.getName());
    }
}
