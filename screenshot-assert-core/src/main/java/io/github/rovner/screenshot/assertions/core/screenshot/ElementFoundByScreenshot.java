package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.exceptions.TooManyElementsException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Takes screenshot of web element that found by selector.
 * Throws {@link NoSuchElementException} if no element is found by selector.
 * Throws {@link TooManyElementsException} if more than one element found by selector.
 */
public final class ElementFoundByScreenshot implements Screenshot {

    private final By selector;
    private String describe;

    ElementFoundByScreenshot(By selector) {
        this.selector = selector;
    }

    public ElementFoundByScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriver webDriver, ImageCropper cropper) {
        List<WebElement> elements = webDriver.findElements(selector);
        if (elements.isEmpty()) {
            throw new NoSuchElementException("No element found by " + selector);
        }
        if (elements.size() > 1) {
            throw new TooManyElementsException("More than one element found by " + selector);
        }
        return new ElementScreenshot(elements.get(0))
                .take(webDriver, cropper);
    }

    @Override
    public String describe() {
        String elementDescribe = String.format("the element found by: %s", selector.toString());
        return describe == null
                ? elementDescribe
                : String.format("%s (%s)", describe, elementDescribe);
    }
}
