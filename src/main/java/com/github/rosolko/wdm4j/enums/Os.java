package com.github.rosolko.wdm4j.enums;

import com.github.rosolko.wdm4j.util.OsDetector;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Os {
    windows("win"),
    linux("linux"),
    osx("mac");

    private final String value;

    Os(final String value) {
        requireNonNull(value, "value must not be null");

        this.value = value;
    }

    public static Os detect() {
        return Os.valueOf(OsDetector.getInstance().getOs());
    }

    public String getValue() {
        return value;
    }
}
