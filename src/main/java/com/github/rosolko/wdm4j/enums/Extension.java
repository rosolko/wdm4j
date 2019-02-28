package com.github.rosolko.wdm4j.enums;

import static java.util.Objects.requireNonNull;

/**
 * Extension enumeration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Extension {
    /**
     * exe extension.
     */
    EXE("exe"),
    /**
     * zip extension.
     */
    ZIP("zip"),
    /**
     * tar.gz extension.
     */
    TAR_GZ("tar.gz"),
    /**
     * tar.bz2 extension.
     */
    TAR_BZ2("tar.bz2");

    private final String value;

    Extension(final String value) {
        requireNonNull(value);

        this.value = value;
    }

    /**
     * Return extension string representative.
     *
     * @return An extension string representative
     */
    public String getValue() {
        return value;
    }
}
