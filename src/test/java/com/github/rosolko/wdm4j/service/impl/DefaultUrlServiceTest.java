package com.github.rosolko.wdm4j.service.impl;

import java.net.MalformedURLException;
import java.net.URL;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.service.UrlService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Default permission service test")
class DefaultUrlServiceTest {
    private UrlService urlService;

    @BeforeAll
    @DisplayName("Create default url service")
    void setUp() {
        urlService = new DefaultUrlService();
    }

    @Test
    @DisplayName("Build url from pattern based on version, platform and extension")
    void ableToBuildDownloadUrl() {
        final var pattern = "https://some-host.html/{version}/{platform}.{extension}";
        final var version = "v0.0.7";
        final var platform = "osx";
        final var extension = Extension.TAR_BZ2.getValue();

        final var result = urlService.buildUrl(pattern, version, platform, Extension.TAR_BZ2);
        assertThat(result).hasToString(String.format("https://some-host.html/%s/%s.%s", version, platform, extension));
    }

    @Test
    @DisplayName("Get file name from url")
    void ableToGetFileNameFromUrl() throws MalformedURLException {
        final var url = new URL("https://some-host.html/some-file-name.tar");

        final var result = urlService.getFileNameFromUrl(url);
        assertThat(result).isEqualTo("some-file-name.tar");
    }
}
