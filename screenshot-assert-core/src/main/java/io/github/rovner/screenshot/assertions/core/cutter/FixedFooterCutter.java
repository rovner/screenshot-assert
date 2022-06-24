package io.github.rovner.screenshot.assertions.core.cutter;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Cuts fixed footer from image.
 */
public class FixedFooterCutter implements Cutter {

    private final int footerHeight;

    public FixedFooterCutter(int footerHeight) {
        this.footerHeight = footerHeight;
    }

    @Override
    public BufferedImage cut(BufferedImage image, WebDriver webDriver, ImageCropper cropper) {
        Rectangle toCrop = new Rectangle(0,
                0,
                image.getHeight() - footerHeight,
                image.getWidth());
        return cropper.crop(image, toCrop);
    }
}
