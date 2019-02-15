package com.github.rosolko.wdm4j.enums;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public enum Extension {
    EXE(".exe"),
    EMPTY(""),
    ZIP("zip"),
    TAR_GZ("tar.gz"),
    TAR_BZ2("tar.bz2");

    private final String value;

    Extension(final String value) {
        requireNonNull(value, "value must not be null");

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
