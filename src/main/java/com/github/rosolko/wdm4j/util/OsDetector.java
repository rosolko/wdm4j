package com.github.rosolko.wdm4j.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import kr.motd.maven.os.Detector;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Utility that allows to get information about current operation system.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public final class OsDetector extends Detector {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private static final Logger logger = getLogger();
    private static OsDetector instance = new OsDetector();
    private final Properties detectedProperties = new Properties();

    private OsDetector() {
        final List<String> keys = Arrays.asList("os.name", "os.arch", "os.version");
        keys.forEach(key -> {
            final String value = System.getProperty(key);
            detectedProperties.put(key, value);
        });
        detect(detectedProperties, Collections.emptyList());
    }

    /**
     * Initialize, identify and store information about current operation system name, architecture and version.
     *
     * @return An instance of OsDetector
     */
    public static OsDetector getInstance() {
        return instance;
    }

    /**
     * Get detected os name.
     *
     * @return A current operation system name
     */
    public String getOs() {
        return (String) instance.detectedProperties.get(Detector.DETECTED_NAME);
    }

    /**
     * Get detected os architecture.
     *
     * @return A current operation system architecture
     */
    public String getArch() {
        return (String) instance.detectedProperties.get(Detector.DETECTED_ARCH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void log(final String message) {
        logger.info(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void logProperty(final String name, final String value) {
        logger.info(name + "=" + value);
    }
}
