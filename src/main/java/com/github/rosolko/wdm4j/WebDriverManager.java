package com.github.rosolko.wdm4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.ArchiveService;
import com.github.rosolko.wdm4j.service.FileService;
import com.github.rosolko.wdm4j.service.PermissionService;
import com.github.rosolko.wdm4j.service.UrlService;
import com.github.rosolko.wdm4j.service.VariableService;
import org.apache.logging.log4j.Logger;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class WebDriverManager {
    private static final Logger log = getLogger();

    public static ConfigStep getInstance() {
        return new Builder();
    }

    public interface ConfigStep {
        SetupStep with(CommonConfig config);
    }

    public interface SetupStep {
        SetupStep version(String version);

        SetupStep os(Os os);

        SetupStep architecture(Architecture architecture);

        void setup();
    }

    private static class Builder implements ConfigStep, SetupStep {
        private final UrlService urlService;
        private final FileService fileService;
        private final ArchiveService archiveService;
        private final PermissionService permissionService;
        private final VariableService variableService;
        private final Queue<Path> queue = new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors() / 2);

        private CommonConfig config;
        private String version;
        private Os os;
        private Architecture architecture;

        private Builder() {
            this.urlService = new UrlService();
            this.fileService = new FileService();
            this.archiveService = new ArchiveService();
            this.permissionService = new PermissionService();
            this.variableService = new VariableService();
        }

        @Override
        public SetupStep with(final CommonConfig config) {
            requireNonNull(config, "config must not be null");

            log.info("Set '{}' config", config);

            this.config = config;
            return this;
        }

        @Override
        public SetupStep version(final String version) {
            this.version = version;
            return this;
        }

        @Override
        public SetupStep os(final Os os) {
            this.os = os;
            return this;
        }

        @Override
        public SetupStep architecture(final Architecture architecture) {
            this.architecture = architecture;
            return this;
        }

        @Override
        public void setup() {
            if (isNull(this.version)) {
                this.version = config.getLatestVersion();
            }
            if (isNull(this.os)) {
                this.os = Os.detect();
            }
            if (isNull(this.architecture)) {
                this.architecture = Architecture.detect();
            }

            log.info("Setup binary with '{}' version, '{}' os and '{}' architecture", version, os, architecture);

            final String pattern = config.getUrlPattern();
            final String platform = config.getPlatform(os, architecture);
            final Extension extension = config.getArchiveExtension(os);
            final String binaryName = config.getBinaryName(os);
            final String binaryVariable = config.getBinaryVariable();
            final Path binaryPath = fileService.getBinaryPath(config.getBrowserName(), version, platform, binaryName);

            while (true) {
                if (!queue.contains(binaryPath)) {
                    queue.add(binaryPath);
                    Path unarchivedBinaryPath;
                    if (!Files.exists(binaryPath)) {
                        final URL url = urlService.buildUrl(pattern, version, platform, extension);
                        final String archiveName = urlService.getFileNameFromUrl(url);
                        final Path archiveTempPath = fileService.getArchiveTempPath(archiveName);
                        final Path downloadedArchive = archiveService.download(url, archiveTempPath);
                        unarchivedBinaryPath = archiveService.extract(downloadedArchive, binaryName, binaryPath, extension);
                        archiveService.remove(downloadedArchive);
                    } else {
                        unarchivedBinaryPath = binaryPath;
                    }
                    permissionService.makeExecutable(os, unarchivedBinaryPath);
                    variableService.setSystemProperty(binaryVariable, unarchivedBinaryPath);
                    break;
                }
            }
            queue.remove(binaryPath);
        }
    }
}
