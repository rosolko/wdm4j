package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class EdgeConfig implements CommonConfig {
    @Override
    public String getBrowserName() {
        return "edge";
    }

    @Override
    public String getBinaryName(final Os os) {
        return "MicrosoftWebDriver.exe";
    }

    @Override
    public String getUrlPattern() {
        return "https://download.microsoft.com/download/F/8/A/F8AF50AB-3C3A-4BC4-8773-DC27B32988DD/MicrosoftWebDriver.exe";
    }

    @Override
    public String getBinaryVariable() {
        return "webdriver.edge.driver";
    }

    @Override
    public String getLatestVersion() {
        return "6.17134";
    }

    @Override
    public Extension getArchiveExtension(final Os os) {
        return Extension.EXE;
    }
}
