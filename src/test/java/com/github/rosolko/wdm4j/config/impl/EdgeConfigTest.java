package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default firefox config test")
class EdgeConfigTest {
    private EdgeConfig config;
    private Architecture architecture;

    @BeforeAll
    @DisplayName("Create default edge config")
    void setUp() {
        config = new EdgeConfig();
        architecture = Architecture.detect();
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final Os os) {
        final var expectedOs = os == Os.LINUX ? Os.OSX : os;
        final var expectedArchitecture = os == Os.OSX ? Architecture.X_86_64 : architecture;
        final var platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .startsWith(expectedOs.getValue())
            .endsWith(expectedArchitecture.getValue());
    }

    @Test
    @DisplayName("Get latest version")
    void ableToGetLatestVersion() {
        final var latestVersion = config.getLatestVersion();
        assertThat(latestVersion).isNotBlank();
        assertThat(latestVersion).matches("^\\d+.\\d+.\\d+.\\d+$");
    }
}
