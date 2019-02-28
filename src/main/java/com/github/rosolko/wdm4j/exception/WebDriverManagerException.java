package com.github.rosolko.wdm4j.exception;

/**
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class WebDriverManagerException extends RuntimeException {
    private static final long serialVersionUID = 8361907324530918044L;

    public WebDriverManagerException(final String message) {
        super(message);
    }

    public WebDriverManagerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
