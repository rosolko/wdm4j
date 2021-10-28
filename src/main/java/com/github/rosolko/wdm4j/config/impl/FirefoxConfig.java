package com.github.rosolko.wdm4j.config.impl;

import java.io.IOException;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import org.jsoup.Jsoup;

import static java.util.Objects.requireNonNull;

/**
 * Firefox configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class FirefoxConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "firefox";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName() {
        return "geckodriver";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://github.com/mozilla/geckodriver/releases/download/v{version}/geckodriver-v{version}-{platform}.{extension}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "webdriver.gecko.driver";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@code "macos"} value for {@link Os#OSX} operation system.
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        return os == Os.OSX
            ? "macos"
            : String.format("%s%s", os.getValue(), architecture.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLatestVersion() {
        try {
            final var document = Jsoup.connect("https://github.com/mozilla/geckodriver/releases").get();
            final var element = document.selectFirst("[class='Link--primary']");
            return requireNonNull(element).text().replace("v", "");
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest firefox webdriver binary version", e);
        }
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock archive extension based on operation system.
     * <br>
     * For windows - {@link Extension#ZIP}
     * <br>
     * For linux/mac - {@link Extension#TAR_GZ}
     */
    @Override
    public Extension getArchiveExtension(final Os os) {
        requireNonNull(os);

        return os == Os.WINDOWS
            ? Extension.ZIP
            : Extension.TAR_GZ;
    }
}
