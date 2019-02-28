package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

/**
 * Variable service interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface VariableService {
    /**
     * Set binary system property based  on binary property value and binary path.
     *
     * @param binaryProperty A binary property
     * @param binaryPath     A binary path
     */
    void setSystemProperty(String binaryProperty, Path binaryPath);
}
