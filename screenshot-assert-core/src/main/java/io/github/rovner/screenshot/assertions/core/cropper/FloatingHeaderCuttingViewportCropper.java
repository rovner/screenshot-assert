package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Cuts header from image, height depends on {@code window.innerHeight}.
 */
public class FloatingHeaderCuttingViewportCropper implements ViewportCropper {

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        long viewportHeight = webDriver.executeScript("return window.innerHeight");
        int scaledViewportHeight = (int) Math.floor(viewportHeight * dpr);
        Rectangle toCrop = new Rectangle(0,
                image.getHeight() - scaledViewportHeight,
                scaledViewportHeight,
                image.getWidth());
        return imageCropper.crop(image, toCrop);
    }
}
