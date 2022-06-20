package io.github.rovner.screenshot.assertions.core.screenshot;

import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Screenshot representation responsible for taking screenshot of various types.
 */
public interface Screenshot extends ScreenshotDescription {

    /**
     * Takes screenshot from browser.
     *
     * @param webDriver driver.
     * @return browser screenshot.
     */
    BufferedImage take(WebDriver webDriver);
}
