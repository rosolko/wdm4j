package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

/**
 * File service interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface FileService {
    /**
     * Get archive temp path by archive name.
     *
     * @param archiveName A binary archive name
     * @return A temporary path on operation system
     */
    Path getArchiveTempPath(String archiveName);

    /**
     * Get binary path based on browser name, binary version, platform and binary name.
     *
     * @param browserName   A browser name
     * @param binaryVersion A binary version
     * @param platform      A platform
     * @param binaryName    A binary name
     * @return Built binary path on operation system
     */
    Path getBinaryPath(String browserName, String binaryVersion, String platform, String binaryName);
}
