package io.github.rovner.screenshot.assertions.core.platform;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Screenshooter representation responsible for taking screenshots from different platforms.
 */
public interface PlatformScreenshoter {

    /**
     * Takes screenshot of viewport.
     *
     * @param webDriver web driver.
     * @param cropper   image cropper.
     * @param scaler    image scaler.
     * @return viewport screenshot.
     */
    BufferedImage takeViewportScreenshot(WebDriver webDriver, ImageCropper cropper, ImageScaler scaler);

    /**
     * Takes screenshot of whole page (document).
     *
     * @param webDriver web driver.
     * @param cropper   image cropper.
     * @param scaler    image scaler.
     * @return whole page screenshot.
     */
    BufferedImage takeWholePageScreenshot(WebDriver webDriver, ImageCropper cropper, ImageScaler scaler);

    /**
     * Test if current screenshooter could take screenshot for target platform.
     *
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    boolean accept(Capabilities capabilities);

    /**
     * Finds first appropriate screenshooter.
     *
     * @param webDriver             web driver
     * @param platformScreenshoters list of screenshooters.
     * @return accepted screenshooter or throws exception if there is none.
     */
    static PlatformScreenshoter findPlatformScreenshooter(WebDriver webDriver, List<PlatformScreenshoter> platformScreenshoters) {
        Capabilities capabilities = ((HasCapabilities) webDriver).getCapabilities();
        return platformScreenshoters.stream()
                .filter(p -> p.accept(capabilities))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "No platform screenshoter is specified for capabilities: %s",
                        capabilities.toString())));
    }
}
