package io.github.rovner.screenshot.assertions.core.scaler;

import io.github.rovner.screenshot.assertions.core.ImageUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Default image scaler that scales with dpr {@code window.outerWidth/image.width()}.
 */
public class DefaultImageScaler implements ImageScaler {
    @Override
    public BufferedImage scale(BufferedImage image, WebDriver webDriver) {
        long width = (long) ((JavascriptExecutor) webDriver).executeScript("return window.outerWidth;");

        if (image.getWidth() == width) {
            return image;
        } else {
            return ImageUtils.scale(image, (double) image.getWidth() / width);
        }
    }
}
