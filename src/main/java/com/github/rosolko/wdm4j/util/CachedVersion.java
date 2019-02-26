package com.github.rosolko.wdm4j.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;

import static java.util.stream.Collectors.joining;

public class CachedVersion {
    private static final String FILE_EXTENSION = Extension.TXT.getValue();
    private static final long LIFE_TIME_IN_DAYS = TimeUnit.DAYS.toMillis(1);
    private final Path cachedVersionPath;

    public CachedVersion(final String browserName) {
        final String dir = System.getProperty("user.dir");
        final String fileName = String.format("%s.%s", browserName, FILE_EXTENSION);
        cachedVersionPath = Paths.get(dir, "build", "binaries", "cache", fileName);
    }

    public boolean isValid() {
        if (!Files.exists(cachedVersionPath)) {
            return false;
        }
        final File file = new File(cachedVersionPath.toString());
        final long lastModified = file.lastModified();
        final long now = ZonedDateTime.now().toInstant().toEpochMilli();
        return now - lastModified <= LIFE_TIME_IN_DAYS;
    }

    public String getFromCache() {
        try {
            return Files.lines(cachedVersionPath).collect(joining());
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to read lines from file", e);
        }
    }

    public void updateCache(final String version) {
        try {
            if (!Files.exists(cachedVersionPath)) {
                Files.createDirectories(cachedVersionPath.getParent());
            }
            Files.write(cachedVersionPath, version.getBytes());
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to write from file", e);
        }
    }
}
