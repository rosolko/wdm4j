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
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@DisplayName("WebDriver manager tests")
class WebDriverManagerTests {
    private static final Logger log = getLogger();

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

    @ParameterizedTest(name = "{arguments}")
    @MethodSource("createConfigs")
    @DisplayName("Get and put binary to system variables")
    void ableToSetUpBinary(final CommonConfig config) {
        log.info("Try to setup WebDriver binary based on '{}' config", config.getClass().getSimpleName());

        final FileService fileService = new FileService();
        final String version = config.getLatestVersion();
        final Os os = Os.detect();
        final Architecture architecture = Architecture.detect();
        final String platform = config.getPlatform(os, architecture);
        final String binaryName = config.getBinaryName(os);
        final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);
        final String variable = config.getBinaryVariable();

        WebDriverManager.getInstance().setup(config);

        final String systemVariable = System.getProperty(variable);
        Assertions.assertTrue(Files.exists(binaryPath));
        Assertions.assertNotNull(systemVariable);
        Assertions.assertEquals(systemVariable, binaryPath.toString());
    }
}
