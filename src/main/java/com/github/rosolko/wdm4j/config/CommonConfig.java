package com.github.rosolko.wdm4j.config;

import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

import static java.util.Objects.requireNonNull;

/**
 * Common configuration interface.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public interface CommonConfig {
    /**
     * Get browser name.
     * <br>
     * Example - {@code "chrome"}.
     *
     * @return A browser name
     */
    String getBrowserName();

    /**
     * Get binary name based on operation system name can it return with extension.
     * <br>
     * Example for windows - {@code "chromedriver.exe"}.
     * <br>
     * Example for mac and linux - {@code "chromedriver"}.
     *
     * @param os A target operation system
     * @return A binary name
     * @see Os
     * @see Extension
     */
    String getBinaryName(Os os);

    /**
     * Get latest binary version.
     *
     * @return A latest binary version
     */
    String getLatestVersion();

    /**
     * Get platform representative based on operation system and architecture.
     * <br>
     * Default pattern - {@code "{os}{architecture}"}.
     * <br>
     * Example - {@code "mac64"}.
     *
     * @param os           A target operation system
     * @param architecture A target architecture
     * @return A platform representative
     * @see Os
     * @see Architecture
     */
    default String getPlatform(Os os, Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        return String.format("%s%s", os.getValue(), architecture.getValue());
    }

    /**
     * Get archive extension based on operation system.
     * <br>
     * Default extension - {@link Extension#ZIP}.
     *
     * @param os A target operation system
     * @return An archive extension
     * @see Os
     * @see Extension
     */
    default Extension getArchiveExtension(Os os) {
        requireNonNull(os);

        return Extension.ZIP;
    }

    /**
     * Get binary download url patter.
     * <br>
     * Example - {@code https://site.com/{version}/binary_{platform}.{extension}}.
     *
     * @return A download url pattern
     */
    String getUrlPattern();

    /**
     * Get binary system variable name.
     * <br>
     * Example - {@code "webdriver.chrome.driver"}.
     *
     * @return A binary system variable name
     */
    String getBinaryVariable();
}
