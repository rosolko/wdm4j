package com.github.rosolko.wdm4j.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.PermissionService;

import static java.util.Objects.requireNonNull;

/**
 * Default permission service implementation.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultPermissionService implements PermissionService {
    /**
     * {@inheritDoc}
     */
    @Override
    public void makeExecutable(final Os os, final Path binaryPath) {
        requireNonNull(os);
        requireNonNull(binaryPath);

        if (os == Os.windows || binaryPath.endsWith(Extension.EXE.getValue())) {
            return;
        }

        if (Files.isExecutable(binaryPath)) {
            return;
        }

        try {
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(binaryPath);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(binaryPath, permissions);
        } catch (final IOException e) {
            throw new WebDriverManagerException("Unable to make binary executable", e);
        }
    }
}
