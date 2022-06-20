package com.github.rovner.screenshot.assertions.core.screenshot;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/**
 * Screenshots factory.
 */
public final class Screenshots {
    public static ElementFoundByScreenshot screenshotOfElementFoundBy(By selector) {
        return new ElementFoundByScreenshot(selector);
    }

    public static AreaScreenshot screenshotOfArea(Rectangle rectangle) {
        return new AreaScreenshot(rectangle);
    }

    public static AreaScreenshot screenshotOfArea(int x, int y, int width, int height) {
        return new AreaScreenshot(new Rectangle(x, y, height, width));
    }

    public static ElementScreenshot screenshotOfElement(WebElement webElement) {
        return new ElementScreenshot(webElement);
    }

    public static Screenshot screenshotOfWholePage() {
        return new WholePageScreenshot();
    }
}
