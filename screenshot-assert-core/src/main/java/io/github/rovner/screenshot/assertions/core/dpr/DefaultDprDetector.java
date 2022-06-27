package io.github.rovner.screenshot.assertions.core.dpr;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;

/**
 * Detects device pixel ratio either from capabilities, either by calculating ${@code screenshot.getWidth()/window.outerWidth}
 */
public class DefaultDprDetector implements DprDetector {
    @Override
    public double detect(WebDriverWrapper webDriver) {
        Object dpr = webDriver.getCapability("pixelRatio");
        if (dpr instanceof Long) {
            return (long) dpr;
        }
        if (dpr instanceof Double) {
            return (double) dpr;
        }
        BufferedImage screenshot = webDriver.takeScreenshot();
        long width = webDriver.executeScript("return window.outerWidth;");

        if (screenshot.getWidth() == width) {
            return 1;
        } else {
            return (double) screenshot.getWidth() / width;
        }
    }
}
