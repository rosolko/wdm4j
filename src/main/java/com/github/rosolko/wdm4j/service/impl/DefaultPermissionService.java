package com.github.rosolko.wdm4j.service.impl;

import com.github.rosolko.wdm4j.enums.Extension;
import com.github.rosolko.wdm4j.enums.Os;
import com.github.rosolko.wdm4j.exception.WebDriverManagerException;
import com.github.rosolko.wdm4j.service.PermissionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static java.util.Objects.requireNonNull;


/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class DefaultPermissionService implements PermissionService {
    @Override
    public void makeExecutable(final Os os, final Path binaryPath) {
        requireNonNull(os, "os must not be null");
        requireNonNull(binaryPath, "binary path must not be null");

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
