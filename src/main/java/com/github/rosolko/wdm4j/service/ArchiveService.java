package com.github.rosolko.wdm4j.service;

import java.net.URL;
import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Extension;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface ArchiveService {
    Path download(URL url, Path archivePath);

    Path extract(Path archivePath, String binaryName, Path binaryPath, Extension extension);

    void remove(Path archivePath);
}
