package io.github.rovner.screenshot.assertions.core.platform;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toBufferedImage;
import static io.github.rovner.screenshot.assertions.core.platform.Platforms.is;
import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.Platform.*;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

/**
 * Takes screenshot from desktop browsers.
 * Scales screenshots for retina displays.
 * Uses {@link ScrollingScreenshoter} to take whole page screenshot.
 */
public class DesktopBrowserScreenshooter extends ScrollingScreenshoter implements PlatformScreenshoter {

    public DesktopBrowserScreenshooter(Duration scrollSleepTimeout) {
        super(scrollSleepTimeout);
    }

    @Override
    public BufferedImage takeViewportScreenshot(WebDriver webDriver, ImageCropper cropper, ImageScaler scaler) {
        BufferedImage image = toBufferedImage(((TakesScreenshot) webDriver).getScreenshotAs(BYTES));
        return scaler.scale(image, webDriver);
    }

    @Override
    public boolean accept(Capabilities capabilities) {
        Platform platform = (Platform) capabilities.getCapability(PLATFORM_NAME);
        return is(platform, WINDOWS) || is(platform, MAC) || is(platform, UNIX);
    }

}
