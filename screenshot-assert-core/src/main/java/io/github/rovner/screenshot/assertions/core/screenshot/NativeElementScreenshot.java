package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.ImageUtils;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Takes screenshot of native element.
 */
public final class NativeElementScreenshot implements KeepContextScreenshot {

    private final WebElement element;
    private String describe;
    private Rectangle rectangle;
    private BufferedImage contextScreenshot;

    NativeElementScreenshot(WebElement element) {
        this.element = element;
    }

    public NativeElementScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        BufferedImage screenshot = webDriver.takeScreenshot();
        double dpr = configuration.getDprDetector().detect(webDriver);
        contextScreenshot = configuration.getImageScaler().scale(screenshot, dpr);
        rectangle = element.getRect();
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
        String elementDescribe = String.format("the element : %s", element.toString());
        return describe == null
                ? elementDescribe
                : String.format("%s (%s)", describe, elementDescribe);
    }

    @Override
    public BufferedImage getContextScreenshot(Color color) {
        return ImageUtils.drawRectangle(contextScreenshot, rectangle, color);
    }
}
