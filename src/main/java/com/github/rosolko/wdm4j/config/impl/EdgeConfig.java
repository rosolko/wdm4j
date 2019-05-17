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

import static org.apache.commons.lang3.StringUtils.split;

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
    public String getBinaryNameWithExtension(final Os os) {
        return "msedgedriver.exe";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://az813057.vo.msecnd.net/webdriver/msedgedriver_{platform}/msedgedriver.exe";
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
            final Document document = Jsoup.connect("https://msedgecdn.azurewebsites.net/webdriver/index.html").get();
            final Element element = document.selectFirst("body > h1");
            final String[] parts = split(element.text(), " ");
            return parts[parts.length - 1];
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to get latest edge webdriver binary version", e);
        }
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@link Extension#EXE} value.
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
        return architecture == Architecture.X_86_32
            ? "x86"
            : "x64";
    }
}
