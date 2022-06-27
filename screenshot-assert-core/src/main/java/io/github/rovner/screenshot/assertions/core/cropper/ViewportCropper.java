package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;

/**
 * Crops viewport from screenshot
 */
public interface ViewportCropper {

    /**
     * Crops viewport from screenshot
     *
     * @param image        screenshot to crop
     * @param imageCropper image cropper used to perform a crop
     * @param webDriver    web driver
     * @param dpr          device pixel ratio
     * @return cropped screenshot.
     */
    BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr);
}
