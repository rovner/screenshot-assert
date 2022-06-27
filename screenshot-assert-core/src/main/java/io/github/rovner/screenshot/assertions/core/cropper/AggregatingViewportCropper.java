package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Cropper that aggregates other viewport croppers by executing them one by one
 */
public class AggregatingViewportCropper implements ViewportCropper {

    private final List<ViewportCropper> croppers;

    public AggregatingViewportCropper(List<ViewportCropper> cropper) {
        this.croppers = cropper;
    }

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        BufferedImage cropped = image;
        for (ViewportCropper cropper : croppers) {
            cropped = cropper.crop(cropped, imageCropper, webDriver, dpr);
        }
        return cropped;
    }
}
