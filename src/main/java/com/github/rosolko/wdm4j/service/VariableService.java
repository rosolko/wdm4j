package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface VariableService {
    void setSystemProperty(String binaryProperty, Path binaryPath);
}
