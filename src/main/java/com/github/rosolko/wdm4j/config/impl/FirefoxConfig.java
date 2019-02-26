package com.github.rosolko.wdm4j.config.impl;

import java.io.IOException;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class FirefoxConfig implements CommonConfig {
    @Override
    public String getBrowserName() {
        return "firefox";
    }

    @Override
    public String getBinaryName(final Os os) {
        requireNonNull(os, "os must not be null");

        final String baseName = "geckodriver";
        return os == Os.windows
            ? String.format("%s.%s", baseName, Extension.EXE.getValue())
            : baseName;
    }

    @Override
    public String getUrlPattern() {
        return "https://github.com/mozilla/geckodriver/releases/download/{version}/geckodriver-{version}-{platform}.{extension}";
    }

    @Override
    public String getBinaryVariable() {
        return "webdriver.gecko.driver";
    }

    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os, "os must not be null");
        requireNonNull(architecture, "architecture must not be null");

        return os == Os.osx
            ? "macos"
            : os.getValue() + architecture.getValue();
    }

    @Override
    public String getLatestVersion() {
        final Document document;
        try {
            document = Jsoup.connect("https://github.com/mozilla/geckodriver/releases").get();
            final Element element = document.selectFirst(".release-header .f1 a");
            return element.text();
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest firefox webdriver binary version", e);
        }
    }

    @Override
    public Extension getArchiveExtension(final Os os) {
        requireNonNull(os, "os must not be null");

        return os == Os.windows
            ? Extension.ZIP
            : Extension.TAR_GZ;
    }
}
