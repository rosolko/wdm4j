package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Os;

public class InternetExplorerConfig implements CommonConfig {
    @Override
    public String getBrowserName() {
        return "internetexplorer";
    }

    @Override
    public String getBinaryName(final Os os) {
        return "IEDriverServer.exe";
    }

    @Override
    public String getLatestVersion() {
        return "3.141";
    }

    @Override
    public String getUrlPattern() {
        return "http://selenium-release.storage.googleapis.com/3.141/IEDriverServer_Win32_3.141.5.zip";
    }

    @Override
    public String getBinaryVariable() {
        return "webdriver.ie.driver";
    }
}
