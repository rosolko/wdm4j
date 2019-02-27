package com.github.rosolko.wdm4j.service;

import java.net.URL;

import com.github.rosolko.wdm4j.enums.Extension;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface UrlService {
    URL buildUrl(String pattern, String version, String platform, Extension extension);

    String getFileNameFromUrl(URL url);
}
