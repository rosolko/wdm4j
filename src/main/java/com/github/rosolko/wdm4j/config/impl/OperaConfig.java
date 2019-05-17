package com.github.rosolko.wdm4j.config.impl;

import java.io.IOException;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Objects.requireNonNull;

/**
 * Opera configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class OperaConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "opera";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName() {
        return "operadriver";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://github.com/operasoftware/operachromiumdriver/releases/download/v.{version}/operadriver_{platform}.{extension}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "webdriver.opera.driver";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock architecture with {@link Architecture#X_86_32} value for {@link Os#WINDOWS} operation system.
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        final Architecture outArchitecture = os == Os.WINDOWS
            ? Architecture.X_86_32
            : architecture;
        return String.format("%s%s", os.getValue(), outArchitecture.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLatestVersion() {
        try {
            final Document document = Jsoup.connect("https://github.com/operasoftware/operachromiumdriver/releases").get();
            final Element element = document.selectFirst(".release-header .f1 a");
            return element.text();
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest opera webdriver binary version", e);
        }
    }
}
