package com.sammwy.libi18n.errors;

public class LanguageLoadException extends Exception {
    public LanguageLoadException(Exception child) {
        super(child);
    }
}
