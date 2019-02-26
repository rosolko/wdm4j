package com.github.rosolko.wdm4j.service.impl;

import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultFileService implements FileService {
    @Override
    public Path getArchiveTempPath(final String archiveName) {
        requireNonNull(archiveName, "archive name must not be null");

        final String tmpdir = System.getProperty("java.io.tmpdir");
        return Paths.get(tmpdir, archiveName);
    }

    @Override
    public Path getBinaryPath(final String browserName, final String version, final String platform,
                              final String binaryName) {
        requireNonNull(browserName, "browser name must not be null");
        requireNonNull(version, "version must not be null");
        requireNonNull(platform, "platform must not be null");
        requireNonNull(binaryName, "binary name must not be null");

        final String dir = System.getProperty("user.dir");
        final String prefix = Paths.get("build", "binaries").toString();
        final Path binaryPath = Paths.get(dir, prefix, browserName, version, platform, binaryName);
        createTargetDirectory(binaryPath);
        return binaryPath;
    }

    private void createTargetDirectory(final Path binaryPath) {
        requireNonNull(binaryPath, "binary path must not be null");

        final Path directory = binaryPath.getParent();

        if (Files.exists(directory)) {
            return;
        }

        try {
            Files.createDirectories(directory);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to create binary target file", e);
        }
    }
}
