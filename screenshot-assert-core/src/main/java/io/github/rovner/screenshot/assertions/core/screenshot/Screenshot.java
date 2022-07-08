package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Set;

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

    /**
     * Shifts absolute coordinates to relative
     * @param areas coordinates to shift
     * @return relative areas
     */
    default Set<Rectangle> shiftAreas(Set<Rectangle> areas) {
        return areas;
    }
}
