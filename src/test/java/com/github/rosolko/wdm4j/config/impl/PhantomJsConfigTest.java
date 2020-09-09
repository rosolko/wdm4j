package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default phantomjs config test")
class PhantomJsConfigTest {
    private PhantomJsConfig config;
    private Architecture architecture;

    @BeforeAll
    @DisplayName("Create default phantomjs config")
    void setUp() {
        config = new PhantomJsConfig();
        architecture = Architecture.detect();
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get platform based on os")
    void ableToGetPlatform(final Os os) {
        final String expectedOs = os == Os.OSX ? "macosx" : os.getDetectValue();
        final String outArchitecture = os == Os.LINUX ? architecture.getDetectValue() : "";

        final String platform = config.getPlatform(os, architecture);
        assertThat(platform)
            .startsWith(expectedOs)
            .endsWith(outArchitecture);
    }

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get archive extension")
    void ableToGetArchiveExtension(final Os os) {
        final Extension extension = os == Os.LINUX ? Extension.TAR_BZ2 : Extension.ZIP;
        final Extension archiveExtension = config.getArchiveExtension(os);
        assertThat(archiveExtension).isEqualTo(extension);
    }
}
