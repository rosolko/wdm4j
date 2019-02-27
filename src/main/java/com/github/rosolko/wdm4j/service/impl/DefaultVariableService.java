package com.github.rosolko.wdm4j.service.impl;

import java.nio.file.Path;

import com.github.rosolko.wdm4j.service.VariableService;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultVariableService implements VariableService {
    @Override
    public void setSystemProperty(final String binaryProperty, final Path binaryPath) {
        requireNonNull(binaryProperty, "binary property must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

        final String stringBinaryPath = binaryPath.toString();
        final String systemPropertyBinaryPath = System.getProperty(binaryProperty);

        if (!isNull(systemPropertyBinaryPath) && systemPropertyBinaryPath.equals(stringBinaryPath)) {
            return;
        }

        System.setProperty(binaryProperty, stringBinaryPath);
    }
}
