package com.github.rosolko.wdm4j.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.service.ArchiveService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default archive service test")
class DefaultArchiveServiceTest {
    private ArchiveService archiveService;

    @BeforeAll
    @DisplayName("Create default archive service")
    void setUp() {
        archiveService = new DefaultArchiveService();
    }

    @Test
    @DisplayName("Download archive from URL to archive path")
    void ableToDownloadArchive(@TempDir final Path tempDir) throws MalformedURLException {
        final var url = new URL("https://chromedriver.storage.googleapis.com/2.27/chromedriver_win32.zip");
        final var archivePath = tempDir.resolve("chromedriver_win32.zip");

        final var result = archiveService.download(url, archivePath);
        assertThat(result)
            .exists()
            .isEqualTo(archivePath);
    }

    @Test
    @DisplayName("Extract variable from archive by extension using binary name to binary path")
    void ableToExtractEntryFromArchive(@TempDir final Path tempDir) throws IOException {
        final var archive = Files.createTempFile("chromedriver", ".zip");
        final var binary = Files.createTempFile("chromedriver", ".txt");
        final var data = Files.readAllBytes(binary);
        final var binaryName = binary.getFileName().toString();
        try (var outputStream = Files.newOutputStream(archive)) {
            try (var zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream)) {
                final var entry = new ZipArchiveEntry(binaryName);
                entry.setSize(data.length);
                zipArchiveOutputStream.putArchiveEntry(entry);
                zipArchiveOutputStream.write(data);
                zipArchiveOutputStream.closeArchiveEntry();
            }
        }
        final var binaryPath = tempDir.resolve(binaryName);

        final var result = archiveService.extract(archive, binaryName, binaryPath, Extension.ZIP);
        assertThat(result)
            .exists()
            .isEqualTo(binaryPath);
    }

    @Test
    @DisplayName("Remove archive by path")
    void ableToRemoveArchive() throws IOException {
        final var archive = File.createTempFile("archive", ".zip");
        final var archivePath = archive.toPath();

        archiveService.remove(archivePath);
        assertThat(archivePath).doesNotExist();
    }
}
