package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Takes screenshot of desired area of viewport.
 */
public class ViewportAreaScreenshot implements Screenshot {

    private final Rectangle rectangle;
    private String describe;

    ViewportAreaScreenshot(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public ViewportAreaScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriver webDriver, ImageCropper cropper,
                              ImageScaler scaler, List<PlatformScreenshoter> screenshooters) {
        BufferedImage bufferedImage = new ViewportScreenshot().take(webDriver, cropper, scaler, screenshooters);
        return cropper.crop(bufferedImage, rectangle);
    }

    @Override
    public String describe() {
        String areaDescribe = String.format("the area [x:%d, y:%d, width:%d, height: %d]",
                rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        return describe == null
                ? areaDescribe
                : String.format("%s (%s)", describe, areaDescribe);
    }
}
