package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Cropper that selects first matching cropper
 */
public class MatchingViewportCropper implements ViewportCropper {

    private final Map<Predicate<WebDriver>, ViewportCropper> croppers = new HashMap<>();

    public MatchingViewportCropper() {
    }

    /**
     * Adds cropper with matcher.
     *
     * @param predicate matcher.
     * @param cropper   cropper to use if matched.
     * @return self.
     */
    public MatchingViewportCropper match(Predicate<WebDriver> predicate, ViewportCropper cropper) {
        this.croppers.put(predicate, cropper);
        return this;
    }

    @Override
    public BufferedImage crop(BufferedImage image, ImageCropper imageCropper, WebDriverWrapper webDriver, double dpr) {
        for (Map.Entry<Predicate<WebDriver>, ViewportCropper> entry : croppers.entrySet()) {
            if (entry.getKey().test(webDriver.getWebDriver())) {
                return entry.getValue().crop(image, imageCropper, webDriver, dpr);
            }
        }
        throw new IllegalStateException("No viewport cropper found that match web driver");
    }
}
