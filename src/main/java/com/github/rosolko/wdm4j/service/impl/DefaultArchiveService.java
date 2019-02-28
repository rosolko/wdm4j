package com.github.rosolko.wdm4j.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.ArchiveService;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Default archive service implementation.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultArchiveService implements ArchiveService {
    /**
     * {@inheritDoc}
     */
    @Override
    public Path download(final URL url, final Path archivePath) {
        requireNonNull(url);
        requireNonNull(archivePath);

        if (Files.exists(archivePath)) {
            return archivePath;
        }

        try (InputStream inputStream = url.openStream()) {
            Files.copy(inputStream, archivePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to download archive from url", e);
        }
        return archivePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path extract(final Path archivePath, final String binaryName, final Path binaryPath,
                        final Extension extension) {
        requireNonNull(archivePath);
        requireNonNull(binaryName);
        requireNonNull(binaryPath);
        requireNonNull(extension);

        if (Files.exists(binaryPath)) {
            return binaryPath;
        }

        switch (extension) {
            case EXE:
                return extractFromExe(archivePath, binaryPath);
            case ZIP:
                return extractFromZip(archivePath, binaryName, binaryPath);
            case TAR_GZ:
                return extractFromTarGz(archivePath, binaryName, binaryPath);
            case TAR_BZ2:
                return extractFromTarBz(archivePath, binaryName, binaryPath);
            default:
                throw new WebDriverManagerException(String.format("Unknown archive type: %s", extension.getValue()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final Path archivePath) {
        requireNonNull(archivePath);

        if (!Files.exists(archivePath)) {
            return;
        }

        try {
            Files.delete(archivePath);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to remove archive", e);
        }
    }

    private Path extractFromExe(final Path archivePath, final Path binaryPath) {
        requireNonNull(archivePath);
        requireNonNull(binaryPath);

        try {
            return Files.copy(archivePath, binaryPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to copy file", e);
        }
    }

    private Path extractFromTarGz(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath);
        requireNonNull(binaryName);
        requireNonNull(binaryPath);

        try (InputStream inputStream = Files.newInputStream(archivePath);
             InputStream gzipCompressorInputStream = new GzipCompressorInputStream(inputStream);
             ArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipCompressorInputStream)) {
            extractEntry(binaryName, binaryPath, tarArchiveInputStream);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from tar.gz archive", e);
        }
        return binaryPath;
    }

    private Path extractFromTarBz(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath);
        requireNonNull(binaryName);
        requireNonNull(binaryPath);

        try (InputStream inputStream = Files.newInputStream(archivePath);
             InputStream bZip2CompressorInputStream = new BZip2CompressorInputStream(inputStream);
             ArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(bZip2CompressorInputStream)) {
            extractEntry(binaryName, binaryPath, tarArchiveInputStream);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from tar.gz archive", e);
        }
        return binaryPath;
    }

    private void extractEntry(final String binaryName, final Path binaryPath,
                              final ArchiveInputStream archiveInputStream) throws IOException {
        requireNonNull(binaryName);
        requireNonNull(binaryPath);
        requireNonNull(archiveInputStream);

        ArchiveEntry entry = archiveInputStream.getNextEntry();
        while (nonNull(entry)) {
            final String entryName = Paths.get(entry.getName()).getFileName().toString();
            if (entryName.equals(binaryName) && !entry.isDirectory()) {
                Files.copy(archiveInputStream, binaryPath, StandardCopyOption.REPLACE_EXISTING);
            }
            entry = archiveInputStream.getNextEntry();
        }
    }

    private Path extractFromZip(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath);
        requireNonNull(binaryName);
        requireNonNull(binaryPath);

        try (InputStream inputStream = Files.newInputStream(archivePath);
             ArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream)) {
            extractEntry(binaryName, binaryPath, zipArchiveInputStream);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from zip archive", e);
        }
        return binaryPath;
    }
}
