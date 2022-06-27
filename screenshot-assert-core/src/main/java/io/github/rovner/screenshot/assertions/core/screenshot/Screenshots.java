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
     *
     * @param selector of web element to screenshot.
     * @return screenshot object.
     */
    public static ElementFoundByScreenshot screenshotOfElementFoundBy(By selector) {
        return new ElementFoundByScreenshot(selector);
    }

    /**
     * Creates screenshot of viewport area.
     *
     * @param rectangle area to screenshot.
     * @return screenshot object.
     */
    public static ViewportAreaScreenshot screenshotOfViewportArea(Rectangle rectangle) {
        return new ViewportAreaScreenshot(rectangle);
    }

    /**
     * Creates screenshot of whole page area.
     *
     * @param rectangle area to screenshot.
     * @return screenshot object.
     */
    public static PageAreaScreenshot screenshotOfPageArea(Rectangle rectangle) {
        return new PageAreaScreenshot(rectangle);
    }

    /**
     * Creates screenshot of viewport area.
     *
     * @param x      x coordinate of area to ignore relative to viewport.
     * @param y      y coordinate of area to ignore relative to viewport.
     * @param width  width of area to ignore.
     * @param height height of area to ignore.
     * @return screenshot object.
     */
    public static ViewportAreaScreenshot screenshotOfViewportArea(int x, int y, int width, int height) {
        return new ViewportAreaScreenshot(new Rectangle(x, y, height, width));
    }

    /**
     * Creates screenshot of viewport area.
     *
     * @param x      x coordinate of area to ignore relative to page.
     * @param y      y coordinate of area to ignore relative to page.
     * @param width  width of area to ignore.
     * @param height height of area to ignore.
     * @return screenshot object.
     */
    public static PageAreaScreenshot screenshotOfPageArea(int x, int y, int width, int height) {
        return new PageAreaScreenshot(new Rectangle(x, y, height, width));
    }

    /**
     * Creates screenshot of element.
     *
     * @param element web element to screenshot.
     * @return screenshot object.
     */
    public static ElementScreenshot screenshotOfElement(WebElement element) {
        return new ElementScreenshot(element);
    }

    /**
     * Creates screenshot of viewport
     *
     * @return screenshot object.
     */
    public static ViewportScreenshot screenshotOfViewport() {
        return new ViewportScreenshot();
    }

    /**
     * Creates screenshot of whole page
     *
     * @return screenshot object.
     */
    public static PageScreenshot screenshotOfWholePage() {
        return new PageScreenshot();
    }
}
