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
    public String getBinaryName(final Os os) {
        return "IEDriverServer.exe";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@code "3.141"} value.
     */
    @Override
    public String getLatestVersion() {
        return "3.141";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "http://selenium-release.storage.googleapis.com/3.141/IEDriverServer_Win32_3.141.5.zip";
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
     * Lock with {@link Os#windows} and {@link Architecture#x86_32} values.
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        return String.format("%s%s", Os.windows.getValue(), Architecture.x86_32.getValue());
    }
}
