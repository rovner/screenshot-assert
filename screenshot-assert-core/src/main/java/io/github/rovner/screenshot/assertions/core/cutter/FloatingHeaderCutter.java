package io.github.rovner.screenshot.assertions.core.cutter;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Cuts header from image, height depends on {@code window.innerHeight}.
 */
public class FloatingHeaderCutter implements Cutter {
    @Override
    public BufferedImage cut(BufferedImage image, WebDriver webDriver, ImageCropper cropper) {
        int viewportHeight = (int) (long) ((JavascriptExecutor) webDriver)
                .executeScript("return window.innerHeight");
        Rectangle toCrop = new Rectangle(0,
                image.getHeight() - viewportHeight,
                viewportHeight,
                image.getWidth());
        return cropper.crop(image, toCrop);
    }
}
