package io.github.rovner.screenshot.assertions.core.cutter;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Cuts fixed header from image.
 */
public class FixedHeaderCutter implements Cutter {

    private final int headerHeight;

    public FixedHeaderCutter(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    @Override
    public BufferedImage cut(BufferedImage image, WebDriver webDriver, ImageCropper cropper) {
        Rectangle toCrop = new Rectangle(0,
                headerHeight,
                image.getHeight() - headerHeight,
                image.getWidth());
        return cropper.crop(image, toCrop);
    }
}
