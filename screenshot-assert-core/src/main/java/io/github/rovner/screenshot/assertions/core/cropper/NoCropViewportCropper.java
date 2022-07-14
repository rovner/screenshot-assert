package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;

/**
 * View cropper implementation that does nothing.
 */
public class NoCropViewportCropper implements ViewportCropper {
    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        return image;
    }
}
