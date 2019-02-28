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
    void download(@TempDir Path tempDir) throws MalformedURLException {
        final URL url = new URL("https://chromedriver.storage.googleapis.com/2.27/chromedriver_win32.zip");
        final Path archivePath = tempDir.resolve("chromedriver_win32.zip");

        final Path result = archiveService.download(url, archivePath);
        assertThat(result)
            .exists()
            .isEqualTo(archivePath);
    }

    @Test
    @DisplayName("Extract variable from archive by extension using binary name to binary path")
    void extract(@TempDir Path tempDir) throws IOException {
        final Path archive = Files.createTempFile("chromedriver", ".zip");
        final Path binary = Files.createTempFile("chromedriver", ".txt");
        final byte[] data = Files.readAllBytes(binary);
        final String binaryName = binary.getFileName().toString();
        try (OutputStream outputStream = Files.newOutputStream(archive)) {
            try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream)) {
                final ZipArchiveEntry entry = new ZipArchiveEntry(binaryName);
                entry.setSize(data.length);
                zipArchiveOutputStream.putArchiveEntry(entry);
                zipArchiveOutputStream.write(data);
                zipArchiveOutputStream.closeArchiveEntry();
            }
        }
        final Path binaryPath = tempDir.resolve(binaryName);

        final Path result = archiveService.extract(archive, binaryName, binaryPath, Extension.ZIP);
        assertThat(result)
            .exists()
            .isEqualTo(binaryPath);
    }

    @Test
    @DisplayName("Remove archive by path")
    void remove() throws IOException {
        final File archive = File.createTempFile("archive", ".zip");
        final Path archivePath = archive.toPath();

        archiveService.remove(archivePath);
        assertThat(archivePath).doesNotExist();
    }
}
