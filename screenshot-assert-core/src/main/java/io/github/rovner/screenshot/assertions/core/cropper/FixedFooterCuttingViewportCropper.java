package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Cuts fixed footer from image.
 */
public class FixedFooterCuttingViewportCropper implements ViewportCropper {

    private final int footerHeight;

    public FixedFooterCuttingViewportCropper(int footerHeight) {
        this.footerHeight = footerHeight;
    }

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        Rectangle toCrop = new Rectangle(0,
                0,
                image.getHeight() - footerHeight,
                image.getWidth());
        return imageCropper.crop(image, toCrop);
    }
}
