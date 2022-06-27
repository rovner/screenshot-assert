package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Takes screenshot of desired area of viewport.
 */
public final class ViewportAreaScreenshot implements Screenshot {

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
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        BufferedImage bufferedImage = new ViewportScreenshot().take(webDriver, configuration);
        return configuration.getImageCropper().crop(bufferedImage, rectangle);
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
