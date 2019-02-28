package com.github.rosolko.wdm4j.enums;

import com.github.rosolko.wdm4j.util.OsDetector;

import static java.util.Objects.requireNonNull;

/**
 * Operation system enumeration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Os {
    /**
     * windows operation system.
     */
    windows("win"),
    /**
     * linux operation system.
     */
    linux("linux"),
    /**
     * macos operation system.
     */
    osx("mac");

    private final String value;

    Os(final String value) {
        requireNonNull(value);

        this.value = value;
    }

    /**
     * Detected current operation system.
     *
     * @return A detected operation system enumeration representative
     * @see OsDetector#getOs
     */
    public static Os detect() {
        return Os.valueOf(OsDetector.getInstance().getOs());
    }

    /**
     * Return operation system string representative.
     *
     * @return An operation system string representative
     */
    public String getValue() {
        return value;
    }
}
