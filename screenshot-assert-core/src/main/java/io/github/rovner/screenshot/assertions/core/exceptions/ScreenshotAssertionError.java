package io.github.rovner.screenshot.assertions.core.exceptions;

import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;

public class ScreenshotAssertionError extends AssertionError {

    private final ImageDiff diff;

    public ScreenshotAssertionError(Object detailMessage, ImageDiff diff) {
        super(detailMessage);
        this.diff = diff;
    }

    public ImageDiff getDiff() {
        return diff;
    }
}
