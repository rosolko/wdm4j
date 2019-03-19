package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

import static java.util.Objects.requireNonNull;

/**
 * PhantomJS configuration.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class PhantomJsConfig implements CommonConfig {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getBrowserName() {
        return "phantomjs";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryName(final Os os) {
        requireNonNull(os);

        final String name = "phantomjs";
        return os == Os.WINDOWS
            ? String.format("%s.%s", name, Extension.EXE.getValue())
            : name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPattern() {
        return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-{version}-{platform}.{extension}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinaryVariable() {
        return "phantomjs.binary.path";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock operation system with {@code "macosx"} value for {@link Os#OSX} operation system.
     * <br>
     * Lock architecture with empty value for windows/mac operation system.
     */
    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os);
        requireNonNull(architecture);

        final String outOs = os == Os.OSX ? "macosx" : os.getDetectValue();
        final String outArchitecture = os == Os.LINUX ? architecture.getDetectValue() : "";
        final String outFormat = outArchitecture.isEmpty() ? "%s%s" : "%s-%s";

        return String.format(outFormat, outOs, outArchitecture);
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock with {@code "2.1.1"} value.
     */
    @Override
    public String getLatestVersion() {
        return "2.1.1";
    }

    /**
     * {@inheritDoc}
     * <br>
     * <br>
     * Lock archive extension based on operation system.
     * <br>
     * For linux - {@link Extension#TAR_BZ2}
     * <br>
     * For windows/mac - {@link Extension#ZIP}
     */
    @Override
    public Extension getArchiveExtension(final Os os) {
        requireNonNull(os);

        return os == Os.LINUX
            ? Extension.TAR_BZ2
            : Extension.ZIP;
    }
}
