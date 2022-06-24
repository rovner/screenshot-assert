package io.github.rovner.screenshot.assertions.core.cutter;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Cuts some parts of image.
 */
public interface Cutter {

    /**
     * Cuts some parts of image.
     *
     * @param image     image to cut.
     * @param webDriver web driver.
     * @param cropper   cropper used to perform a cut
     * @return cut image.
     */
    BufferedImage cut(BufferedImage image, WebDriver webDriver, ImageCropper cropper);
}
