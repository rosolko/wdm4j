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
    void buildUrl() {
        final String pattern = "https://some-host.html/{version}/{platform}.{extension}";
        final String version = "v0.0.7";
        final String platform = "osx";
        final String extension = Extension.TAR_BZ2.getValue();

        final URL result = urlService.buildUrl(pattern, version, platform, Extension.TAR_BZ2);
        assertThat(result).hasToString(String.format("https://some-host.html/%s/%s.%s", version, platform, extension));
    }

    @Test
    @DisplayName("Get file name from url")
    void getFileNameFromUrl() throws MalformedURLException {
        final URL url = new URL("https://some-host.html/some-file-name.tar");

        final String result = urlService.getFileNameFromUrl(url);
        assertThat(result).isEqualTo("some-file-name.tar");
    }
}
