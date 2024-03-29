package com.github.rosolko.wdm4j.config.impl;

import java.io.IOException;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import org.jsoup.Jsoup;

import static java.util.Objects.requireNonNull;

/**
 * Edge configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class EdgeConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "edge";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName() {
        return "msedgedriver";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://msedgedriver.azureedge.net/{version}/edgedriver_{platform}.{extension}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "webdriver.edge.driver";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLatestVersion() {
        try {
            final var document = Jsoup.connect("https://msedgedriver.azureedge.net/LATEST_BETA").ignoreContentType(true).get();
            return document.body().text();
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest edge webdriver binary version", e);
        }
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock architectures based on operation system.
     * <br>
     * For mac/linux - {@link Architecture#X_86_64}
     */
    @SuppressWarnings("PMD.SwitchStmtsShouldHaveDefault")
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        Os outOs;
        Architecture outArchitecture;
        switch (os) {
            case WINDOWS -> {
                outOs = Os.WINDOWS;
                outArchitecture = architecture;
            }
            case LINUX, OSX -> {
                outOs = Os.OSX;
                outArchitecture = Architecture.X_86_64;
            }
            default -> throw new IllegalStateException("Unexpected value: " + os);
        }
        return String.format("%s%s", outOs.getValue(), outArchitecture.getValue());
    }
}
