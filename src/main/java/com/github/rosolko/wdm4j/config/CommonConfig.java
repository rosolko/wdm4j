package com.github.rosolko.wdm4j.config;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface CommonConfig {
    String getBrowserName();

    String getBinaryName(Os os);

    String getLatestVersion();

    default String getPlatform(Os os, Architecture architecture) {
        requireNonNull(os, "os must not be null");
        requireNonNull(architecture, "architecture must not be null");

        return String.format("%s%s", os.getValue(), architecture.getValue());
    }

    default Extension getArchiveExtension(Os os) {
        requireNonNull(os, "os must not be null");

        return Extension.ZIP;
    }

    String getUrlPattern();

    String getBinaryVariable();
}
