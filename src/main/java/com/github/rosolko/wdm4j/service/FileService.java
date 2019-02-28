package com.github.rosolko.wdm4j.service;

import java.nio.file.Path;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface FileService {
    Path getArchiveTempPath(String archiveName);

    Path getBinaryPath(String browserName, String binaryVersion, String platform, String binaryName);
}
