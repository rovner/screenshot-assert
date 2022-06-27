package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Crops viewport based on web driver capabilities
 */
public class CapabilitiesViewportCropper implements ViewportCropper {

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        Map<String, Long> viewportRect = webDriver.getCapability("viewportRect");
        int x = Math.toIntExact(viewportRect.get("left"));
        int y = Math.toIntExact(viewportRect.get("top"));
        int width = Math.toIntExact(viewportRect.get("width"));
        int height = Math.toIntExact(viewportRect.get("height"));
        return imageCropper.crop(image, new Rectangle(x, y, height, width));
    }
}
