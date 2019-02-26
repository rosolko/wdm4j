package com.github.rosolko.wdm4j;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.*;
import com.github.rosolko.wdm4j.service.impl.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public final class WebDriverManager {
    private final Queue<Path> queue = new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors());

    private final UrlService urlService;
    private final FileService fileService;
    private final ArchiveService archiveService;
    private final PermissionService permissionService;
    private final VariableService variableService;

    public WebDriverManager() {
        this(new UrlServiceImpl(), new FileServiceImpl(), new ArchiveServiceImpl(),
            new PermissionServiceImpl(), new VariableServiceImpl());
    }

    public WebDriverManager(final UrlService urlService, final FileService fileService,
                            final ArchiveService archiveService, final PermissionService permissionService,
                            final VariableService variableService) {
        this.urlService = urlService;
        this.fileService = fileService;
        this.archiveService = archiveService;
        this.permissionService = permissionService;
        this.variableService = variableService;
    }

    public void setup(final CommonConfig config) {
        final String version = config.getLatestVersion();
        final Os os = Os.detect();
        final Architecture architecture = Architecture.detect();
        setup(config, version, os, architecture);
    }

    public void setup(final CommonConfig config, final String version) {
        final Os os = Os.detect();
        final Architecture architecture = Architecture.detect();
        setup(config, version, os, architecture);
    }

    private void setup(final CommonConfig config, final String version, final Os os, final Architecture architecture) {
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
