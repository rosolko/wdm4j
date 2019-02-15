package com.github.rosolko.wdm4j.enums;

import com.github.rosolko.wdm4j.util.OsDetector;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Architecture {
    x86_32("32"),
    x86_64("64"),
    i686("68");

    private final String value;

    Architecture(final String value) {
        requireNonNull(value, "value must not be null");

        this.value = value;
    }

    public static Architecture detect() {
        return Architecture.valueOf(OsDetector.getInstance().getArch());
    }

    public String getValue() {
        return value;
    }
}
