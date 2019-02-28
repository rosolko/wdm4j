package com.github.rosolko.wdm4j.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.rosolko.wdm4j.service.FileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default archive service test")
class DefaultFileServiceTest {
    private FileService fileService;

    @BeforeAll
    @DisplayName("Create default file service")
    void setUp() {
        fileService = new DefaultFileService();
    }

    @Test
    @DisplayName("Get archive temp path by archive name")
    void getArchiveTempPath() throws IOException {
        final Path archiveTempPath = Files.createTempFile("chromedriver", ".tar.gz");
        final String archiveName = archiveTempPath.getFileName().toString();

        final Path result = fileService.getArchiveTempPath(archiveName);
        assertThat(result).isEqualTo(archiveTempPath);
    }

    @Test
    @DisplayName("Get binary path based on browser name, binary version, platform and binary name")
    void getBinaryPath() {
        final String browserName = "testBrowserName";
        final String version = "4.1.01235";
        final String platform = "windows64";
        final String binaryName = "testBinary.exe";
        final Path expectedPath = Paths.get(System.getProperty("user.dir"),
            "build", "binaries", browserName, version, platform, binaryName);

        final Path binaryPath = fileService.getBinaryPath(browserName, version, platform, binaryName);
        assertThat(binaryPath).isEqualTo(expectedPath);
    }
}
