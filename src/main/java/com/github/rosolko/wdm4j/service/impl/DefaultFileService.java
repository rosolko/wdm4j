package com.github.rosolko.wdm4j.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.FileService;

import static java.util.Objects.requireNonNull;

/**
 * Default file service implementation.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultFileService implements FileService {
    /**
     * {@inheritDoc}
     */
    @Override
    public Path getArchiveTempPath(final String archiveName) {
        requireNonNull(archiveName);

        final String tmpdir = System.getProperty("java.io.tmpdir");
        return Paths.get(tmpdir, archiveName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getBinaryPath(final String browserName, final String binaryVersion, final String platform,
                              final String binaryName) {
        requireNonNull(browserName);
        requireNonNull(binaryVersion);
        requireNonNull(platform);
        requireNonNull(binaryName);

        final String userDirectory = System.getProperty("user.dir");
        final String prefix = Paths.get("build", "binaries").toString();
        final Path binaryPath = Paths.get(userDirectory, prefix, browserName, binaryVersion, platform, binaryName);
        createTargetDirectory(binaryPath);
        return binaryPath;
    }

    private void createTargetDirectory(final Path binaryPath) {
        requireNonNull(binaryPath);

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
