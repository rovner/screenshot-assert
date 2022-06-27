package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.exceptions.TooManyElementsException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Takes screenshot of web element that found by selector.
 * Throws {@link NoSuchElementException} if no element is found by selector.
 * Throws {@link TooManyElementsException} if more than one element found by selector.
 */
public final class ElementFoundByScreenshot implements KeepContextScreenshot {

    private final By selector;
    private String describe;
    private KeepContextScreenshot delegate;

    ElementFoundByScreenshot(By selector) {
        this.selector = selector;
    }

    public ElementFoundByScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        List<WebElement> elements = webDriver.getWebDriver().findElements(selector);
        if (elements.isEmpty()) {
            throw new NoSuchElementException("No element found by " + selector);
        }
        if (elements.size() > 1) {
            throw new TooManyElementsException("More than one element found by " + selector);
        }
        delegate = new ElementScreenshot(elements.get(0));
        return delegate.take(webDriver, configuration);
    }

    @Override
    public String describe() {
        String elementDescribe = String.format("the element found by: %s", selector.toString());
        return describe == null
                ? elementDescribe
                : String.format("%s (%s)", describe, elementDescribe);
    }

    @Override
    public BufferedImage getContextScreenshot(Color color) {
        return delegate.getContextScreenshot(color);
    }
}
