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
public enum Architecture {
    /**
     * x86_32 bit operation system.
     */
    X_86_32("x86_32", "32"),
    /**
     * x86_64 bit operation system.
     */
    X_86_64("x86_64", "64"),
    /**
     * i686 bit operation system.
     */
    I_686("i686", "68");

    private final String detectValue;
    private final String value;

    Architecture(final String detectValue, final String value) {
        requireNonNull(detectValue);
        requireNonNull(value);

        this.detectValue = detectValue;
        this.value = value;
    }

    /**
     * Detected current architecture.
     *
     * @return A detected architecture enumeration representative
     * @see OsDetector#getArch
     */
    public static Architecture detect() {
        return Architecture.ofValue(OsDetector.getInstance().getArch());
    }

    private static Architecture ofValue(final String architecture) {
        return Arrays.stream(Architecture.values())
            .filter(value -> value.getDetectValue().equalsIgnoreCase(architecture))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Unknown architecture: '" + architecture + "'"));
    }

    /**
     * Return architecture detect string representative.
     *
     * @return An architecture detect string representative
     */
    public String getDetectValue() {
        return detectValue;
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
