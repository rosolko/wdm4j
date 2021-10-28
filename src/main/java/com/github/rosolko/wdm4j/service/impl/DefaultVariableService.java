package com.github.rosolko.wdm4j.service.impl;

import java.nio.file.Path;

import com.github.rosolko.wdm4j.service.VariableService;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Default variable service implementation.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultVariableService implements VariableService {
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSystemProperty(final String binaryProperty, final Path binaryPath) {
        requireNonNull(binaryProperty);
        requireNonNull(binaryPath);

        final var stringBinaryPath = binaryPath.toString();
        final var systemPropertyBinaryPath = System.getProperty(binaryProperty);

        if (!isNull(systemPropertyBinaryPath) && systemPropertyBinaryPath.equals(stringBinaryPath)) {
            return;
        }

        System.setProperty(binaryProperty, stringBinaryPath);
    }
}
