package com.github.rosolko.wdm4j.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.github.rosolko.wdm4j.util.OsDetector;

import static java.util.Objects.requireNonNull;

/**
 * Operation system enumeration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@SuppressWarnings("PMD.FieldNamingConventions")
public enum Os {
    /**
     * windows operation system.
     */
    WINDOWS("windows", "win"),
    /**
     * linux operation system.
     */
    LINUX("linux", "linux"),
    /**
     * macos operation system.
     */
    OSX("osx", "mac");

    private final String detectValue;
    private final String value;

    Os(final String detectValue, final String value) {
        requireNonNull(detectValue);
        requireNonNull(value);

        this.detectValue = detectValue;
        this.value = value;
    }

    /**
     * Detected current operation system.
     *
     * @return A detected operation system enumeration representative
     * @see OsDetector#getOs
     */
    public static Os detect() {
        return Os.ofValue(OsDetector.getInstance().getOs());
    }

    private static Os ofValue(final String os) {
        return Arrays.stream(Os.values())
            .filter(value -> value.getDetectValue().equalsIgnoreCase(os))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Unknown os: '" + os + "'"));
    }

    /**
     * Return operation system detect string representative.
     *
     * @return An operation system detect string representative
     */
    public String getDetectValue() {
        return detectValue;
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
