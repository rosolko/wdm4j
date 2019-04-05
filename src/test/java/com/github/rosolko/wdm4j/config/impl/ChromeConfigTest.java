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
@DisplayName("Default chrome config test")
class ChromeConfigTest {
    private ChromeConfig config;
    private Architecture architecture;

    @BeforeAll
    @DisplayName("Create default chrome config")
    void setUp() {
        config = new ChromeConfig();
        architecture = Architecture.detect();
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final Os os) {
        final Architecture outArchitecture = os == Os.WINDOWS ? Architecture.X_86_32 : Architecture.X_86_64;
        final String platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .startsWith(os.getValue())
            .endsWith(outArchitecture.getValue());
    }

    @Test
    @DisplayName("Get latest version")
    void ableToGetLatestVersion() {
        final String latestVersion = config.getLatestVersion();
        assertThat(latestVersion).isNotBlank();
        assertThat(latestVersion).matches("^\\d+\\.\\d+.\\d+.\\d+$");
    }
}
