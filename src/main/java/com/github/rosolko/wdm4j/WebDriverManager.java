package com.github.rosolko.wdm4j;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.github.rosolko.wdm4j.config.CommonConfig;
import com.github.rosolko.wdm4j.config.impl.ChromeConfig;
import com.github.rosolko.wdm4j.config.impl.EdgeConfig;
import com.github.rosolko.wdm4j.config.impl.FirefoxConfig;
import com.github.rosolko.wdm4j.config.impl.InternetExplorerConfig;
import com.github.rosolko.wdm4j.config.impl.OperaConfig;
import com.github.rosolko.wdm4j.config.impl.PhantomJsConfig;
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

/**
 * WebDriverManager entry point.
 *
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

    /**
     * Initialize WebDriverManager with default service implementation.
     *
     * @see DefaultArchiveService
     * @see DefaultFileService
     * @see DefaultPermissionService
     * @see DefaultUrlService
     * @see DefaultVariableService
     */
    public WebDriverManager() {
        this(new DefaultArchiveService(), new DefaultFileService(), new DefaultPermissionService(),
            new DefaultUrlService(), new DefaultVariableService());
    }

    /**
     * Initialize WebDriverManager with custom service implementation.
     * <br>
     * This case you can use default service implementation from {@link com.github.rosolko.wdm4j.service.impl} package
     * as well as your own service implementation from {@link com.github.rosolko.wdm4j.service} package.
     *
     * @param archiveService    An {@link ArchiveService} implementation
     * @param fileService       A {@link FileService} implementation
     * @param permissionService A {@link PermissionService} implementation
     * @param urlService        A {@link UrlService} implementation
     * @param variableService   A {@link VariableService} implementation
     */
    public WebDriverManager(final ArchiveService archiveService, final FileService fileService,
                            final PermissionService permissionService, final UrlService urlService,
                            final VariableService variableService) {
        this.archiveService = archiveService;
        this.fileService = fileService;
        this.permissionService = permissionService;
        this.urlService = urlService;
        this.variableService = variableService;
    }

    /**
     * Setup WebDriver binary based on config.
     * <br>
     * This case you can use default configuration implementation from {@link com.github.rosolko.wdm4j.config.impl}
     * package as well as your own configuration implementation of {@link CommonConfig}.
     * <br>
     * Available predefined configurations: {@link ChromeConfig}, {@link EdgeConfig}, {@link FirefoxConfig},
     * {@link InternetExplorerConfig}, {@link OperaConfig}, {@link PhantomJsConfig}.
     *
     * @param config A configuration upon which the setup will be based
     */
    public void setup(final CommonConfig config) {
        setup(config, config.getLatestVersion(), Os.detect(), Architecture.detect());
    }

    /**
     * Perform basic setup with binary version override.
     *
     * @param config A configuration upon which the setup will be based
     * @param version An exact binary version
     *
     * @see WebDriverManager#setup(CommonConfig)
     */
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
