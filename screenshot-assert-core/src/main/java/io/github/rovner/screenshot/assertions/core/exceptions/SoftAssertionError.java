package io.github.rovner.screenshot.assertions.core.exceptions;

public class SoftAssertionError extends AssertionError {
    public SoftAssertionError(Object detailMessage) {
        super(detailMessage);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
