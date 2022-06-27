package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static java.lang.Math.toIntExact;

/**
 * Takes screenshot of web element.
 */
public final class ElementScreenshot implements KeepContextScreenshot {

    private final WebElement element;
    private String describe;
    KeepContextScreenshot delegate;

    ElementScreenshot(WebElement element) {
        this.element = element;
    }

    public ElementScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        Map<String, Object> map = webDriver.executeScript("" +
                "var element = arguments[0];\n" +
                "var area = {};\n" +
                "var rect = element.getBoundingClientRect();\n" +
                "area.width = Math.round(rect.width);\n" +
                "area.height = Math.round(rect.height);\n" +
                "var screenX = rect.x - window.scrollX;\n" +
                "var screenY = rect.y - window.scrollY;\n" +
                "if (rect.x < 0 || rect.y < 0 || rect.right > window.innerWidth || rect.bottom > window.innerHeight) {\n" +
                "    area.visible = false;\n" +
                "    area.x = Math.round(rect.x + window.scrollX);\n" +
                "    area.y = Math.round(rect.y + window.scrollY); \n" +
                "} else {\n" +
                "    area.visible = true;\n" +
                "    area.x = Math.round(rect.x);\n" +
                "    area.y = Math.round(rect.y);\n" +
                "}\n" +
                "return area;", element);
        boolean isVisible = (boolean) map.get("visible");
        Rectangle rectangle = new Rectangle(
                toIntExact((Long) map.get("x")),
                toIntExact((Long) map.get("y")),
                toIntExact((Long) map.get("height")),
                toIntExact((Long) map.get("width"))
        );

        delegate = isVisible
                ? new ViewportAreaScreenshot(rectangle)
                : new PageAreaScreenshot(rectangle);
        return delegate.take(webDriver, configuration);
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
        return delegate.getContextScreenshot(color);
    }
}
