package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Os;

/**
 * Internet explorer configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class InternetExplorerConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "internetexplorer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName() {
        return "IEDriverServer";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryNameWithExtension(final Os os) {
        return "IEDriverServer.exe";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@code "3.150"} value.
     */
    @Override
    public String getLatestVersion() {
        return "3.150";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "http://selenium-release.storage.googleapis.com/{version}/IEDriverServer_{platform}_{version}.1.zip";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "webdriver.ie.driver";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@link Os#WINDOWS} and {@link Architecture#X_86_32} values.
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        return architecture == Architecture.X_86_32 ? "Win32" : "x64";
    }
}
