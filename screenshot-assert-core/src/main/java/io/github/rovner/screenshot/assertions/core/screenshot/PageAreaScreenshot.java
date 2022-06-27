package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

/**
 * Takes screenshot of desired area of the whole pager
 */
public final class PageAreaScreenshot implements Screenshot {
    private final Rectangle rectangle;

    private String describe;

    PageAreaScreenshot(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public PageAreaScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        BufferedImage bufferedImage = new PageScreenshot().take(webDriver, configuration);
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
