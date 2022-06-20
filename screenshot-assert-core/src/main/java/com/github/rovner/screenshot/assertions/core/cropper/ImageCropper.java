package com.github.rovner.screenshot.assertions.core.cropper;

import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Crops desired area from image
 */
public interface ImageCropper {
    /**
     * Crops desired area from image
     *
     * @param original   image to crop from
     * @param areaToCrop coordinates of area to be cropped
     * @return cropped image
     */
    BufferedImage crop(BufferedImage original, Rectangle areaToCrop);
}
