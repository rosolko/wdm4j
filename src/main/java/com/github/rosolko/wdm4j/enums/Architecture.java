package com.github.rosolko.wdm4j.enums;

import com.github.rosolko.wdm4j.util.OsDetector;

import static java.util.Objects.requireNonNull;

/**
 * Operation system enumeration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Architecture {
    /**
     * x86_32 bit operation system.
     */
    x86_32("32"),
    /**
     * x86_64 bit operation system.
     */
    x86_64("64"),
    /**
     * i686 bit operation system.
     */
    i686("68");

    private final String value;

    Architecture(final String value) {
        requireNonNull(value);

        this.value = value;
    }

    /**
     * Detected current architecture.
     *
     * @return A detected architecture enumeration representative
     * @see OsDetector#getArch
     */
    public static Architecture detect() {
        return Architecture.valueOf(OsDetector.getInstance().getArch());
    }

    /**
     * Return architecture string representative.
     *
     * @return An architecture string representative
     */
    public String getValue() {
        return value;
    }
}
