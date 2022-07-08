package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.ImageUtils;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Takes screenshot of desired area of viewport.
 */
public final class ViewportAreaScreenshot implements KeepContextScreenshot {

    private final Rectangle rectangle;
    private String describe;
    private BufferedImage contextScreenshot;

    ViewportAreaScreenshot(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public ViewportAreaScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        contextScreenshot = new ViewportScreenshot().take(webDriver, configuration);
        return configuration.getImageCropper().crop(contextScreenshot, rectangle);
    }

    @Override
    public Set<Rectangle> shiftAreas(Set<Rectangle> areas) {
        return areas.stream()
                .map(toShift -> new Rectangle(
                        toShift.x - rectangle.x,
                        toShift.y - rectangle.y,
                        toShift.height,
                        toShift.width))
                .collect(Collectors.toSet());
    }

    @Override
    public String describe() {
        String areaDescribe = String.format("the area [x:%d, y:%d, width:%d, height: %d]",
                rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        return describe == null
                ? areaDescribe
                : String.format("%s (%s)", describe, areaDescribe);
    }

    @Override
    public BufferedImage getContextScreenshot(Color color) {
        return ImageUtils.drawRectangle(contextScreenshot, rectangle, color);
    }
}
