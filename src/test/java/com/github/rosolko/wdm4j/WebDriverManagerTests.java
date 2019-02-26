package com.github.rosolko.wdm4j;

import java.nio.file.Files;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@DisplayName("WebDriver manager tests")
class WebDriverManagerTests {
    private Os os;
    private Architecture architecture;
    private FileService fileService;
    private FirefoxConfig firefoxConfig;

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

    @DisplayName("Detect os and architecture, initialize file service and default config")
    @BeforeEach
    void setUp() {
        os = Os.detect();
        architecture = Architecture.detect();
        fileService = new FileService();
        firefoxConfig = new FirefoxConfig();
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

        WebDriverManager.getInstance().config(config).setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override version and setup binary")
    void ableToOverrideVersion() {
        final String version = "v0.23.0";

        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .version(version)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override os and setup binary")
    void ableToOverrideOs() {
        final Os os = Os.linux;

        final String version = firefoxConfig.getLatestVersion();
        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .os(os)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override architecture and setup binary")
    void ableToOverrideArchitecture() {
        final Architecture architecture = Architecture.x86_32;

        final String version = firefoxConfig.getLatestVersion();
        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .architecture(architecture)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override os and architecture and setup binary")
    void ableToOverrideOsAndArchitecture() {
        final Os os = Os.linux;
        final Architecture architecture = Architecture.x86_32;

        final String version = firefoxConfig.getLatestVersion();
        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .os(os)
            .architecture(architecture)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override version and architecture and setup binary")
    void ableToOverrideVersionAndArchitecture() {
        final String version = "v0.22.0";
        final Architecture architecture = Architecture.x86_64;

        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .version(version)
            .architecture(architecture)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override version and os and setup binary")
    void ableToOverrideVersionAndOs() {
        final String version = "v0.22.0";
        final Os os = Os.windows;

        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .version(version)
            .os(os)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }

    @Test
    @DisplayName("Override version, os and architecture and setup binary")
    void ableToOverrideVersionAndOsAndArchitecture() {
        final String version = "v0.22.0";
        final Os os = Os.linux;
        final Architecture architecture = Architecture.x86_32;

        final String platform = firefoxConfig.getPlatform(os, architecture);
        final String binaryName = firefoxConfig.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(firefoxConfig.getBrowserName(), version, platform, binaryName);
        final String variable = firefoxConfig.getBinaryVariable();

        WebDriverManager.getInstance()
            .config(firefoxConfig)
            .version(version)
            .os(os)
            .architecture(architecture)
            .setup();

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }
}
