package com.github.rosolko.wdm4j.config.impl;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;

import static java.util.Objects.requireNonNull;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class PhantomJsConfig implements CommonConfig {
    @Override
    public String getBrowserName() {
        return "phantomjs";
    }

    @Override
    public String getBinaryName(final Os os) {
        requireNonNull(os, "os must not be null");

        final String name = "phantomjs";
        return os == Os.windows
            ? String.format("%s.%s", name, Extension.EXE.getValue())
            : name;
    }

    @Override
    public String getUrlPattern() {
        return "https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-{version}-{platform}.{extension}";
    }

    @Override
    public String getBinaryVariable() {
        return "phantomjs.binary.path";
    }

    @Override
    public String getPlatform(final Os os, final Architecture architecture) {
        requireNonNull(os, "os must not be null");
        requireNonNull(architecture, "architecture must not be null");

        final String outOs = os == Os.osx ? "macosx" : os.toString();
        final String outArchitecture = os == Os.linux ? architecture.toString() : "";
        final String outFormat = outArchitecture.equals("") ? "%s%s" : "%s-%s";

        return String.format(outFormat, outOs, outArchitecture);
    }

    @Override
    public String getLatestVersion() {
        return "2.1.1";
    }

    @Override
    public Extension getArchiveExtension(final Os os) {
        requireNonNull(os, "os must not be null");

        return os == Os.linux
            ? Extension.TAR_BZ2
            : Extension.ZIP;
    }
}
