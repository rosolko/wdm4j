package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
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
class FirefoxConfigTest {
    private FirefoxConfig config;
    private Architecture architecture;

    @BeforeAll
    @DisplayName("Create default firefox config")
    void setUp() {
        config = new FirefoxConfig();
        architecture = Architecture.detect();
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final Os os) {
        final String expectedArchitecture = os == Os.OSX ? "" : architecture.getValue();
        final String platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .startsWith(os.getValue())
            .endsWith(expectedArchitecture);
    }

    @Test
    @DisplayName("Get latest version")
    void ableToGetLatestVersion() {
        final String latestVersion = config.getLatestVersion();
        assertThat(latestVersion).isNotBlank();
        assertThat(latestVersion).matches("^v\\d+\\.\\d+.\\d+$");
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get archive extension")
    void ableToGetArchiveExtension(final Os os) {
        final Extension extension = os == Os.WINDOWS ? Extension.ZIP : Extension.TAR_GZ;
        final Extension archiveExtension = config.getArchiveExtension(os);
        assertThat(archiveExtension).isEqualTo(extension);
    }
}
