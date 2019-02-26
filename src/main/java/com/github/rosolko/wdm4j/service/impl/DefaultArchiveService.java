package com.github.rosolko.wdm4j.service.impl;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.ArchiveService;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultArchiveService implements ArchiveService {
    @Override
    public Path download(final URL url, final Path archivePath) {
        requireNonNull(url, "url must not be null");
        requireNonNull(archivePath, "archive path must not be null");

        if (Files.exists(archivePath)) {
            return archivePath;
        }

        try (InputStream in = url.openStream()) {
            Files.copy(in, archivePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to download archive from url", e);
        }
        return archivePath;
    }

    @Override
    public Path extract(final Path archivePath, final String binaryName, final Path binaryPath,
                        final Extension extension) {
        requireNonNull(archivePath, "archive path must not be null");
        requireNonNull(binaryName, "binary name must not be null");
        requireNonNull(binaryPath, "binary path must not be null");
        requireNonNull(extension, "extension must not be null");

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

    @Override
    public void remove(final Path archivePath) {
        requireNonNull(archivePath, "archive path must not be null");

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
        requireNonNull(archivePath, "archive path must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

        try {
            return Files.copy(archivePath, binaryPath);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to copy file", e);
        }
    }

    private Path extractFromTarGz(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath, "archive path must not be null");
        requireNonNull(binaryName, "binary name must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

        try (InputStream fi = Files.newInputStream(archivePath);
             InputStream gzi = new GzipCompressorInputStream(fi);
             ArchiveInputStream ti = new TarArchiveInputStream(gzi)) {
            extractEntry(binaryName, binaryPath, ti);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from tar.gz archive", e);
        }
        return binaryPath;
    }

    private Path extractFromTarBz(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath, "archive path must not be null");
        requireNonNull(binaryName, "binary name must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

        try (InputStream fi = Files.newInputStream(archivePath);
             InputStream gzi = new BZip2CompressorInputStream(fi);
             ArchiveInputStream ti = new TarArchiveInputStream(gzi)) {
            extractEntry(binaryName, binaryPath, ti);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from tar.gz archive", e);
        }
        return binaryPath;
    }

    private void extractEntry(final String binaryName, final Path binaryPath,
                              final ArchiveInputStream archiveInputStream) throws IOException {
        requireNonNull(binaryName, "binary name must not be null");
        requireNonNull(binaryPath, "binary path must not be null");
        requireNonNull(archiveInputStream, "archive input stream must not be null");

        ArchiveEntry entry;
        while ((entry = archiveInputStream.getNextEntry()) != null) {
            final String name = Paths.get(entry.getName()).getFileName().toString();
            if (name.equals(binaryName) && !entry.isDirectory()) {
                try (OutputStream o = Files.newOutputStream(binaryPath)) {
                    IOUtils.copy(archiveInputStream, o);
                }
            }
        }
    }

    private Path extractFromZip(final Path archivePath, final String binaryName, final Path binaryPath) {
        requireNonNull(archivePath, "archive name must not be null");
        requireNonNull(binaryName, "binary name must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

        try (InputStream fi = Files.newInputStream(archivePath);
             ArchiveInputStream zi = new ZipArchiveInputStream(fi)) {
            extractEntry(binaryName, binaryPath, zi);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to extract binary from zip archive", e);
        }
        return binaryPath;
    }
}
