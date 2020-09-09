package com.github.rosolko.wdm4j.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.PermissionService;
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
@DisplayName("Default permission service test")
class DefaultPermissionServiceTest {
    private PermissionService permissionService;

    @BeforeAll
    @DisplayName("Create default permission service")
    void setUp() {
        permissionService = new DefaultPermissionService();
    }

    @Test
    @DisplayName("Make binary executable by binary path based on os")
    void ableToMakeBinaryExecutable() throws IOException {
        final Path binaryPath = Files.createTempFile("chromedriver", ".sh");

        permissionService.makeExecutable(Os.LINUX, binaryPath);
        assertThat(binaryPath).isExecutable();
    }
}
