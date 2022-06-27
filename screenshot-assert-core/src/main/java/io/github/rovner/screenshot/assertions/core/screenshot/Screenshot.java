package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;

/**
 * Screenshot representation responsible for taking screenshot of various types.
 */
public interface Screenshot extends ScreenshotDescription {

    /**
     * Takes screenshot from browser.
     *
     * @param webDriver     web driver.
     * @param configuration screenshot configuration.
     * @return browser screenshot.
     */
    BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration);
}
