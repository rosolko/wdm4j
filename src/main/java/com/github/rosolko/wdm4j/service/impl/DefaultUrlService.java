package com.github.rosolko.wdm4j.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.UrlService;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultUrlService implements UrlService {
    @Override
    public URL buildUrl(final String pattern, final String version, final String platform, final Extension extension) {
        requireNonNull(pattern, "pattern must not be null");
        requireNonNull(version, "version must not be null");
        requireNonNull(platform, "platform must not be null");
        requireNonNull(extension, "extension must not be null");

        final String candidate = pattern
            .replaceAll("(?i)(\\{version})", version)
            .replaceAll("(?i)(\\{platform})", platform)
            .replaceAll("(?i)(\\{extension})", extension.getValue());

        try {
            return new URL(candidate);
        } catch (final MalformedURLException e) {
            throw new WebDriverManagerException("Unable to build url based on pattern", e);
        }
    }

    @Override
    public String getFileNameFromUrl(final URL url) {
        requireNonNull(url, "url must not be null");

        final Path path = Paths.get(url.getPath());
        return path.getFileName().toString();
    }
}
