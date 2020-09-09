package com.github.rosolko.wdm4j.config;

import com.github.rosolko.wdm4j.config.impl.ChromeConfig;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default config test")
class CommonConfigTest {

    @ParameterizedTest
    @EnumSource(Os.class)
    @DisplayName("Get binary name based on os")
    void ableToGetBinaryName(final Os os) {
        final ChromeConfig config = new ChromeConfig();
        final String binaryName = config.getBinaryName();
        final String extension = os == Os.WINDOWS ? Extension.EXE.getValue() : "";
        final String binaryNameWithExtension = config.getBinaryNameWithExtension(os);
        assertThat(binaryNameWithExtension)
            .startsWith(binaryName)
            .endsWith(extension);
    }
}
