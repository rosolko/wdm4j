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
 * Default url service implementation.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultUrlService implements UrlService {
    /**
     * {@inheritDoc}
     */
    @Override
    public URL buildUrl(final String pattern, final String version, final String platform, final Extension extension) {
        requireNonNull(pattern);
        requireNonNull(version);
        requireNonNull(platform);
        requireNonNull(extension);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameFromUrl(final URL url) {
        requireNonNull(url);

        final Path path = Paths.get(url.getPath());
        return path.getFileName().toString();
    }
}
