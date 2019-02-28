package com.github.rosolko.wdm4j.service.impl;

import java.nio.file.Path;

import com.github.rosolko.wdm4j.service.VariableService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default permission service test")
class DefaultVariableServiceTest {
    private VariableService variableService;

    @BeforeAll
    @DisplayName("Create default variable service")
    void setUp() {
        variableService = new DefaultVariableService();
    }

    @Test
    @DisplayName("Set system property by name and path")
    void setSystemProperty(@TempDir Path tempPath) {
        final String tempBinaryProperty = "temp.prop";
        final Path tempBinaryPath = tempPath.resolve("binary.exe");

        variableService.setSystemProperty(tempBinaryProperty, tempBinaryPath);
        final String actual = System.getProperty(tempBinaryProperty);
        assertThat(actual)
            .isNotNull()
            .isEqualTo(tempBinaryPath.toString());
    }
}
