package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default firefox config test")
class EdgeConfigTest {
    private EdgeConfig config;
    private Os os;

    @BeforeAll
    @DisplayName("Create default edge config")
    void setUp() {
        config = new EdgeConfig();
        os = Os.detect();
    }

    @ParameterizedTest
    @CsvSource({"X_86_32,x86", "X_86_64,x64"})
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final String architectureString, final String expectedPlatform) {
        final Architecture architecture = Architecture.valueOf(architectureString);
        final String platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .isEqualTo(expectedPlatform);
    }

    @Test
    @DisplayName("Get latest version")
    void ableToGetLatestVersion() {
        final String latestVersion = config.getLatestVersion();
        assertThat(latestVersion).isNotBlank();
        assertThat(latestVersion).matches("^\\d+\\.\\d+\\.\\d+.\\d+$");
    }
}
