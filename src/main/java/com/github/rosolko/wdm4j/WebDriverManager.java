package com.github.rosolko.wdm4j;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.enums.Architecture;
import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.service.ArchiveService;
import com.github.rosolko.wdm4j.service.FileService;
import com.github.rosolko.wdm4j.service.PermissionService;
import com.github.rosolko.wdm4j.service.UrlService;
import com.github.rosolko.wdm4j.service.VariableService;
import com.github.rosolko.wdm4j.service.impl.DefaultArchiveService;
import com.github.rosolko.wdm4j.service.impl.DefaultFileService;
import com.github.rosolko.wdm4j.service.impl.DefaultPermissionService;
import com.github.rosolko.wdm4j.service.impl.DefaultUrlService;
import com.github.rosolko.wdm4j.service.impl.DefaultVariableService;

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

    private final ArchiveService archiveService;
    private final FileService fileService;
    private final PermissionService permissionService;
    private final UrlService urlService;
    private final VariableService variableService;

    public WebDriverManager() {
        this(new DefaultArchiveService(), new DefaultFileService(), new DefaultPermissionService(),
            new DefaultUrlService(), new DefaultVariableService());
    }

    public WebDriverManager(final ArchiveService archiveService, final FileService fileService,
                            final PermissionService permissionService, final UrlService urlService,
                            final VariableService variableService) {
        this.archiveService = archiveService;
        this.fileService = fileService;
        this.permissionService = permissionService;
        this.urlService = urlService;
        this.variableService = variableService;
    }

    public void setup(final CommonConfig config) {
        setup(config, config.getLatestVersion(), Os.detect(), Architecture.detect());
    }

    public void setup(final CommonConfig config, final String version) {
        setup(config, version, Os.detect(), Architecture.detect());
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
