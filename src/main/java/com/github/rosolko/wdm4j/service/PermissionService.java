package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Os;

/**
 * Permission service interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface PermissionService {
    /**
     * Make binary executable by binary path based on os.
     *
     * @param os         A target operation system
     * @param binaryPath A binary path
     */
    void makeExecutable(Os os, Path binaryPath);
}
