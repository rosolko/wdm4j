package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Os;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface PermissionService {
    void makeExecutable(Os os, Path binaryPath);
}
