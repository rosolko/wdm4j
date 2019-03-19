package com.github.rosolko.wdm4j;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.config.impl.ChromeConfig;
import com.github.rosolko.wdm4j.config.impl.EdgeConfig;
import com.github.rosolko.wdm4j.config.impl.FirefoxConfig;
import com.github.rosolko.wdm4j.config.impl.InternetExplorerConfig;
import com.github.rosolko.wdm4j.config.impl.OperaConfig;
import com.github.rosolko.wdm4j.config.impl.PhantomJsConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.FileService;
import com.github.rosolko.wdm4j.service.impl.DefaultFileService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("WebDriver manager test")
class WebDriverManagerTest {
    private Os os;
    private Architecture architecture;
    private FileService fileService;
    private WebDriverManager webDriverManager;

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static Stream<CommonConfig> createConfigs() {
        return Stream.of(
            new ChromeConfig(),
            new EdgeConfig(),
            new FirefoxConfig(),
            new InternetExplorerConfig(),
            new OperaConfig(),
            new PhantomJsConfig()
        );
    }

    @BeforeAll
    @DisplayName("Detect os and architecture, initialize file service")
    void setUp() {
        os = Os.detect();
        architecture = Architecture.detect();
        fileService = new DefaultFileService();
        webDriverManager = new WebDriverManager();
    }

    @ParameterizedTest
    @MethodSource("createConfigs")
    @DisplayName("Setup binary for specific config")
    void ableToSetUpBinary(final CommonConfig config) {
        final String version = config.getLatestVersion();
        final String platform = config.getPlatform(os, architecture);
        final String binaryName = config.getBinaryNameWithExtension(os);
        final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);
        final String variable = config.getBinaryVariable();

        webDriverManager.setup(config);

        final String systemVariable = System.getProperty(variable);
        assertThat(binaryPath).exists();
        assertThat(systemVariable).isNotEmpty();
        assertThat(systemVariable).isEqualTo(binaryPath.toString());
    }

    @Test
    @DisplayName("Setup binary with specific version")
    void ableToSetUpBinaryWithSpecificVersion() {
        final String version = "v0.23.0";

        final CommonConfig config = new FirefoxConfig();
        final String platform = config.getPlatform(os, architecture);
        final String binaryName = config.getBinaryNameWithExtension(os);
        final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);
        final String variable = config.getBinaryVariable();

        webDriverManager.setup(config, version);

        final String systemVariable = System.getProperty(variable);
        assertThat(binaryPath).exists();
        assertThat(systemVariable).isNotEmpty();
        assertThat(systemVariable).isEqualTo(binaryPath.toString());
    }
}
