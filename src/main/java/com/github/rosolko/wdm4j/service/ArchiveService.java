package com.github.rosolko.wdm4j.service;

import java.net.URL;
import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Extension;

/**
 * Archive service interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface ArchiveService {
    /**
     * Download archive from URL to archive path.
     *
     * @param url         A binary archive download url
     * @param archivePath A binary archive path on the operation system
     * @return A binary archive path on the operation system
     */
    Path download(URL url, Path archivePath);

    /**
     * Extract variable from archive by extension using binary name to binary path.
     *
     * @param archivePath A binary archive path on the operation system
     * @param binaryName  A binary name
     * @param binaryPath  A binary path on the operation system
     * @param extension   A binary archive extension
     * @return A binary path on the operation system
     */
    Path extract(Path archivePath, String binaryName, Path binaryPath, Extension extension);

    /**
     * Remove archive from operation system by path.
     *
     * @param archivePath A binary archive path on the operation system
     */
    void remove(Path archivePath);
}
