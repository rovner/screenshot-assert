package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Takes screenshot of desired area.
 */
public class AreaScreenshot implements Screenshot {

    private final Rectangle rectangle;
    private String describe;

    AreaScreenshot(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public AreaScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriver webDriver, ImageCropper cropper) {
        BufferedImage bufferedImage = new ViewportScreenshot().take(webDriver, cropper);
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
