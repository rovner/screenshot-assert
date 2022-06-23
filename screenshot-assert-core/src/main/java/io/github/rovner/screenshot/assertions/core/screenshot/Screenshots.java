package io.github.rovner.screenshot.assertions.core.screenshot;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/**
 * Screenshots factory.
 */
public final class Screenshots {

    private Screenshots() {
    }

    /**
     * Creates screenshot of element found by selector.
     * @param selector of web element to screenshot.
     * @return screenshot object.
     */
    public static ElementFoundByScreenshot screenshotOfElementFoundBy(By selector) {
        return new ElementFoundByScreenshot(selector);
    }

    /**
     * Creates screenshot of area.
     * @param rectangle area to screenshot.
     * @return screenshot object.
     */
    public static AreaScreenshot screenshotOfArea(Rectangle rectangle) {
        return new AreaScreenshot(rectangle);
    }

    /**
     * Creates screenshot of area.
     * @param x x coordinate of area to ignore.
     * @param y y coordinate of area to ignore.
     * @param width width of area to ignore.
     * @param height height of area to ignore.
     * @return screenshot object.
     */
    public static AreaScreenshot screenshotOfArea(int x, int y, int width, int height) {
        return new AreaScreenshot(new Rectangle(x, y, height, width));
    }

    /**
     * Creates screenshot of element.
     * @param element web element to screenshot.
     * @return screenshot object.
     */
    public static ElementScreenshot screenshotOfElement(WebElement element) {
        return new ElementScreenshot(element);
    }

    /**
     * Creates screenshot of viewport
     * @return screenshot object.
     */
    public static Screenshot screenshotOfViewport() {
        return new ViewportScreenshot();
    }
}
