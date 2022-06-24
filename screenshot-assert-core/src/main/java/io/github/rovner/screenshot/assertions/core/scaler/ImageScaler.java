package io.github.rovner.screenshot.assertions.core.scaler;

import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Scales image to real size, e.g. for retina displays.
 */
public interface ImageScaler {
    /**
     * Scales image to real size, e.g. for retina displays.
     *
     * @param image     image to scale
     * @param webDriver web driver
     * @return scaled image.
     */
    BufferedImage scale(BufferedImage image, WebDriver webDriver);
}
