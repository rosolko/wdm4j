package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

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
    public String getBinaryName(final Os os) {
        return "MicrosoftWebDriver.exe";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://download.microsoft.com/download/F/8/A/F8AF50AB-3C3A-4BC4-8773-DC27B32988DD/MicrosoftWebDriver.exe";
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
        return "6.17134";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Extension getArchiveExtension(final Os os) {
        return Extension.EXE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        return String.format("%s%s", Os.windows.getValue(), Architecture.x86_64.getValue());
    }
}
