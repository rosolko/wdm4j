package com.github.rosolko.wdm4j.service;

import java.net.URL;

import com.github.rosolko.wdm4j.enums.Extension;

/**
 * Url service interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface UrlService {
    /**
     * Build url from pattern based on version, platform and extension.
     *
     * @param pattern   A binary download url pattern
     * @param version   A binary version
     * @param platform  A platform
     * @param extension A binary archive extension
     * @return Built binary download url
     * @see Extension
     */
    URL buildUrl(String pattern, String version, String platform, Extension extension);

    /**
     * Get file name from url.
     *
     * @param url A binary download url
     * @return A binary file name
     */
    String getFileNameFromUrl(URL url);
}
