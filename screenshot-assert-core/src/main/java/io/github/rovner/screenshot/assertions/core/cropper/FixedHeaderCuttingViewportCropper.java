package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Cuts fixed header from image.
 */
public class FixedHeaderCuttingViewportCropper implements ViewportCropper {

    private final int headerHeight;

    public FixedHeaderCuttingViewportCropper(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        Rectangle toCrop = new Rectangle(0,
                headerHeight,
                image.getHeight() - headerHeight,
                image.getWidth());
        return imageCropper.crop(image, toCrop);
    }
}
