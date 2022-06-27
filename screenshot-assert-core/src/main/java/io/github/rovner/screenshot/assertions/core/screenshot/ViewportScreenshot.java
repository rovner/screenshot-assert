package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

import java.awt.image.BufferedImage;

/**
 * Takes screenshot of the browser viewport (whole visible page).
 */
public final class ViewportScreenshot implements Screenshot {

    ViewportScreenshot() {
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        BufferedImage screenshot = webDriver.takeScreenshot();
        double dpr = configuration.getDprDetector().detect(webDriver);
        screenshot = configuration.getViewportCropper().crop(screenshot, configuration.getImageCropper(), webDriver, dpr);
        return configuration.getImageScaler().scale(screenshot, dpr);
    }

    @Override
    public String describe() {
        return "the viewport";
    }
}
