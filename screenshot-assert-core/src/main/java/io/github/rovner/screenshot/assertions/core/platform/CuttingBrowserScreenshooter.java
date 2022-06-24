package io.github.rovner.screenshot.assertions.core.platform;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cutter.Cutter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Collection;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toBufferedImage;
import static java.util.Collections.singletonList;
import static org.openqa.selenium.OutputType.BYTES;

/**
 * Takes screenshot from mobile browsers.
 * Cuts mobile bars (phone bars, browser bars) using {@link Cutter}.
 * Uses {@link ScrollingScreenshoter} to take whole page screenshot.
 */
public abstract class CuttingBrowserScreenshooter extends ScrollingScreenshoter {

    private final Collection<Cutter> cutters;

    public CuttingBrowserScreenshooter(Duration scrollSleepTimeout, Cutter cutter) {
        super(scrollSleepTimeout);
        this.cutters = singletonList(cutter);
    }

    public CuttingBrowserScreenshooter(Duration scrollSleepTimeout, Collection<Cutter> cutters) {
        super(scrollSleepTimeout);
        this.cutters = cutters;
    }

    @Override
    public BufferedImage takeViewportScreenshot(WebDriver webDriver, ImageCropper cropper, ImageScaler scaler) {
        BufferedImage image = toBufferedImage(((TakesScreenshot) webDriver).getScreenshotAs(BYTES));
        image = scaler.scale(image, webDriver);
        for (Cutter cutter : cutters) {
            image = cutter.cut(image, webDriver, cropper);
        }
        return image;
    }
}
