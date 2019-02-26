package com.github.rosolko.wdm4j.service;

import com.github.rosolko.wdm4j.enums.Os;

import java.nio.file.Path;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface PermissionService {
    void makeExecutable(Os os, Path binaryPath);
}
