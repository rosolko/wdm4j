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

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public final class WebDriverManager {
    private static WebDriverManager instance = new WebDriverManager();

    private final UrlService urlService;
    private final FileService fileService;
    private final ArchiveService archiveService;
    private final PermissionService permissionService;
    private final VariableService variableService;

    private final Queue<Path> queue = new ArrayBlockingQueue<>(10);

    private WebDriverManager() {
        this.urlService = new UrlService();
        this.fileService = new FileService();
        this.archiveService = new ArchiveService();
        this.permissionService = new PermissionService();
        this.variableService = new VariableService();
    }

    public static synchronized WebDriverManager getInstance() {
        return instance;
    }

    public void manage(final CommonConfig config) {
        final Os os = Os.detect();
        final Architecture architecture = Architecture.detect();
        final String pattern = config.getUrlPattern();
        final String version = config.getLatestVersion();
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
