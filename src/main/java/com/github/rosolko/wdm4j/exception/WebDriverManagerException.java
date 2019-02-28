package com.github.rosolko.wdm4j.exception;

/**
 * Exception for WebDriverManager.
 *
 * @author Aliaksandr Rasolka
 * @since 1.0.0
 */
public class WebDriverManagerException extends RuntimeException {
    private static final long serialVersionUID = 8361907324530918044L;

    /**
     * WebDriverManager exception that accept custom message.
     *
     * @param message A custom message
     */
    public WebDriverManagerException(final String message) {
        super(message);
    }

    /**
     * WebDriverManager exception that accept custom message and throwable cause.
     *
     * @param message A custom message
     * @param cause   A throwable cause
     */
    public WebDriverManagerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
