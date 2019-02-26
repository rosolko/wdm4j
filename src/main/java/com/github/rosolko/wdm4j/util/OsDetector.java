package com.github.rosolko.wdm4j.util;

import kr.motd.maven.os.Detector;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public final class OsDetector extends Detector {
    private static final Logger log = getLogger();
    private static OsDetector instance = new OsDetector();
    private final Properties detectedProperties = new Properties();

    private OsDetector() {
        final List<String> keys = Arrays.asList("os.name", "os.arch", "os.version");
        keys.forEach((key) -> {
            final String value = System.getProperty(key);
            detectedProperties.put(key, value);
        });
        detect(detectedProperties, Collections.emptyList());
    }

    public static OsDetector getInstance() {
        return instance;
    }

    public String getOs() {
        return (String) instance.detectedProperties.get(Detector.DETECTED_NAME);
    }

    public String getArch() {
        return (String) instance.detectedProperties.get(Detector.DETECTED_ARCH);
    }

    @Override
    protected void log(final String message) {
        log.info(message);
    }

    @Override
    protected void logProperty(final String name, final String value) {
        log.info(name + "=" + value);
    }
}
