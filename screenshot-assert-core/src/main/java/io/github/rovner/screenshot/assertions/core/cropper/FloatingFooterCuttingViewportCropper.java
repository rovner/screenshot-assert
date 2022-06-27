package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Cuts footer from image, height depends on {@code window.innerHeight}.
 */
public class FloatingFooterCuttingViewportCropper implements ViewportCropper {
    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        long viewportHeight = webDriver.executeScript("return window.innerHeight");
        Rectangle areaToCrop = new Rectangle(0,
                0,
                (int) Math.floor(viewportHeight * dpr),
                image.getWidth());
        return imageCropper.crop(image, areaToCrop);
    }
}
