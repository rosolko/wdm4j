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
public class ChromeConfig implements CommonConfig {
    @Override
    public String getBrowserName() {
        return "chrome";
    }

    @Override
    public String getBinaryName(final Os os) {
        requireNonNull(os, "os must not be null");

        final String baseName = "chromedriver";
        return os == Os.windows
            ? String.format("%s.%s", baseName, Extension.EXE.getValue())
            : baseName;
    }

    @Override
    public String getUrlPattern() {
        return "https://chromedriver.storage.googleapis.com/{version}/chromedriver_{platform}.{extension}";
    }

    @Override
    public String getBinaryVariable() {
        return "webdriver.chrome.driver";
    }

    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os, "os must not be null");
        requireNonNull(architecture, "architecture must not be null");

        final Architecture outArchitecture = os == Os.windows
            ? Architecture.x86_32
            : Architecture.x86_64;
        return String.format("%s%s", os.getValue(), outArchitecture.getValue());
    }

    @Override
    public String getLatestVersion() {
        final Document document;
        try {
            document = Jsoup.connect("https://chromedriver.storage.googleapis.com/LATEST_RELEASE").get();
            final Element element = document.selectFirst("body");
            return element.text();
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest chrome webdriver binary version", e);
        }
    }
}
