package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

import static io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter.findPlatformScreenshooter;

/**
 * Takes screenshot of the whole page.
 */
public final class PageScreenshot implements Screenshot {

    PageScreenshot() {
    }

    @Override
    public BufferedImage take(WebDriver webDriver, ImageCropper cropper,
                              ImageScaler scaler, List<PlatformScreenshoter> screenshooters) {
        return findPlatformScreenshooter(webDriver, screenshooters)
                .takeWholePageScreenshot(webDriver, cropper, scaler);
    }

    @Override
    public String describe() {
        return "the whole page";
    }
}
