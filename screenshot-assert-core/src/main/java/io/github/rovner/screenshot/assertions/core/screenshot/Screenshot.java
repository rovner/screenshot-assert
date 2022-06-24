package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Screenshot representation responsible for taking screenshot of various types.
 */
public interface Screenshot extends ScreenshotDescription {

    /**
     * Takes screenshot from browser.
     *
     * @param webDriver      web driver.
     * @param cropper        image cropper used to crop image.
     * @param scaler         scaler used to scale screenshots, e.g. for retina displays
     * @param screenshooters list of platform screenshoters.
     * @return browser screenshot.
     */
    BufferedImage take(WebDriver webDriver,
                       ImageCropper cropper,
                       ImageScaler scaler,
                       List<PlatformScreenshoter> screenshooters);
}
