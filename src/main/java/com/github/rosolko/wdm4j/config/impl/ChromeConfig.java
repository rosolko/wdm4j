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
 * Chrome configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class ChromeConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "chrome";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName(final Os os) {
        requireNonNull(os);

        final String name = "chromedriver";
        return os == Os.windows
            ? String.format("%s.%s", name, Extension.EXE.getValue())
            : name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://chromedriver.storage.googleapis.com/{version}/chromedriver_{platform}.{extension}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "webdriver.chrome.driver";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock architectures based on operation system.
     * <br>
     * For windows - {@link Architecture#x86_32}
     * <br>
     * For linux/mac - {@link Architecture#x86_64}
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        final Architecture outArchitecture = os == Os.windows
            ? Architecture.x86_32
            : Architecture.x86_64;
        return String.format("%s%s", os.getValue(), outArchitecture.getValue());
    }

    /**
     * {@inheritDoc}
     */
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
