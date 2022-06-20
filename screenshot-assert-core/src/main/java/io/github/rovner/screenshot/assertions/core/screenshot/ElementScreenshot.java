package io.github.rovner.screenshot.assertions.core.screenshot;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;

/**
 * Takes screenshot of web element.
 */
public final class ElementScreenshot extends CroppedScreenshot<ElementScreenshot> implements Screenshot {


    private final WebElement element;
    private String describe;

    ElementScreenshot(WebElement element) {
        this.element = element;
    }

    public ElementScreenshot as(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    public BufferedImage take(WebDriver webDriver) {
        return new AreaScreenshot(element.getRect())
                .croppedWith(imageCropper)
                .take(webDriver);
    }

    @Override
    public String describe() {
        String elementDescribe = String.format("the element : %s", element.toString());
        return describe == null
                ? elementDescribe
                : String.format("%s (%s)", describe, elementDescribe);
    }
}
