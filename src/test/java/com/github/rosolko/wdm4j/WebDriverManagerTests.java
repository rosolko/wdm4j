package com.github.rosolko.wdm4j;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.config.impl.*;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.FileService;
import com.github.rosolko.wdm4j.service.impl.DefaultFileService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@DisplayName("WebDriver manager tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebDriverManagerTests {
    private Os os;
    private Architecture architecture;
    private FileService fileService;
    private WebDriverManager webDriverManager;

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

    @DisplayName("Detect os and architecture, initialize file service")
    @BeforeAll
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
        final String binaryName = config.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);
        final String variable = config.getBinaryVariable();

        webDriverManager.setup(config);

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Setup binary with specific version")
    void ableToSetUpBinaryWithSpecificVersion() {
        final String version = "v0.23.0";

        final CommonConfig config = new FirefoxConfig();
        final String platform = config.getPlatform(os, architecture);
        final String binaryName = config.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);
        final String variable = config.getBinaryVariable();

        webDriverManager.setup(config, version);

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }
}
