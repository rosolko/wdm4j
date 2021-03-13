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
@DisplayName("Default opera config test")
class OperaConfigTest {
    private OperaConfig config;
    private Architecture architecture;

    @BeforeAll
    @DisplayName("Create default opera config")
    void setUp() {
        config = new OperaConfig();
        architecture = Architecture.detect();
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final Os os) {
        final Architecture expectedArchitecture = os == Os.WINDOWS ? Architecture.X_86_32 : architecture;
        final String platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .startsWith(os.getValue())
            .endsWith(expectedArchitecture.getValue());
    }

    @Test
    @DisplayName("Get latest version")
    void ableToGetLatestVersion() {
        final String latestVersion = config.getLatestVersion();
        assertThat(latestVersion)
            .isNotBlank()
            .matches("^\\d+.\\d+.\\d+.\\d+$");
    }
}
