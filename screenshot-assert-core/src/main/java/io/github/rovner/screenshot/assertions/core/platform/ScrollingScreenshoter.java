package io.github.rovner.screenshot.assertions.core.platform;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static java.lang.Math.toIntExact;

/**
 * Takes screenshot of the whole page by scrolling it from top to bottom and gluing parts together.
 */
@Slf4j
public abstract class ScrollingScreenshoter implements PlatformScreenshoter {

    private final long scrollSleepTimeout;

    public ScrollingScreenshoter(Duration scrollSleepTimeout) {
        this.scrollSleepTimeout = scrollSleepTimeout.toMillis();
    }

    @Override
    public BufferedImage takeWholePageScreenshot(WebDriver webDriver, ImageCropper cropper, ImageScaler scaler) {
        log.warn("Taking full page screenshot! This may cause page state change because requires page to be scrolled!");
        //noinspection unchecked
        Map<String, Long> dimensions = (Map<String, Long>) ((JavascriptExecutor) webDriver)
                .executeScript("return {\n" +
                        "    viewportWidth: window.innerWidth, \n" +
                        "    viewportHeight: window.innerHeight,\n" +
                        "    documentWidth: document.body.scrollWidth,\n" +
                        "    documentHeight: document.body.scrollHeight\n" +
                        "};");

        int viewportWidth = toIntExact(dimensions.get("viewportWidth"));
        int viewportHeight = toIntExact(dimensions.get("viewportHeight"));
        int documentWidth = toIntExact(dimensions.get("documentWidth"));
        int documentHeight = toIntExact(dimensions.get("documentHeight"));
        BufferedImage finalImage = new BufferedImage(documentWidth, documentHeight, TYPE_3BYTE_BGR);
        Graphics2D graphics = finalImage.createGraphics();

        int scrollYTimes = (int) Math.ceil((double) documentHeight / viewportHeight);
        int scrollXTimes = (int) Math.ceil((double) documentWidth / viewportWidth);

        for (int x = 0; x < scrollXTimes; x++) {
            for (int y = 0; y < scrollYTimes; y++) {
                int tailHeight = documentHeight - y * viewportHeight;
                int tailWidth = documentWidth - x * viewportWidth;
                int cropY = tailHeight < viewportHeight ? viewportHeight - tailHeight : 0;
                int cropX = tailWidth < viewportWidth ? viewportWidth - tailWidth : 0;
                int cropWidth = viewportWidth - cropX;
                int cropHeight = viewportHeight - cropY;
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(arguments[0], arguments[1]);",
                        x * viewportWidth - cropX, y * viewportHeight - cropY);

                sleep();

                BufferedImage part = takeViewportScreenshot(webDriver, cropper, scaler);
                part = cropper.crop(part, new Rectangle(cropX, cropY, cropHeight, cropWidth));

                graphics.drawImage(part, x * viewportWidth, y * viewportHeight, null);
            }
        }

        graphics.dispose();
        return finalImage;
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(scrollSleepTimeout);
    }

}
