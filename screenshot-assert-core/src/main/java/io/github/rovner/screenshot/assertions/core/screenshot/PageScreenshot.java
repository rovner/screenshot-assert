package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static java.lang.Math.toIntExact;

/**
 * Takes screenshot of the whole page.
 */
@Slf4j
public final class PageScreenshot implements Screenshot {


    PageScreenshot() {
    }

    @Override
    public BufferedImage take(WebDriverWrapper webDriver, ScreenshotConfiguration configuration) {
        log.warn("Taking full page screenshot! This may cause page state change because requires page to be scrolled!");

        double dpr = configuration.getDprDetector().detect(webDriver);
        double scrollMargin = dpr * configuration.getScrollMarginPixels();
        if ((scrollMargin % 1) != 0) {
            throw new IllegalStateException(String.format("ScrollMarginPixels * dpr should be an integer." +
                    " Provided values dpr: %s, scrollMarginPixels: %s", dpr, configuration.getScrollMarginPixels()));
        }

        Map<String, Long> dimensions = webDriver.executeScript("return {\n" +
                "    viewportWidth: window.innerWidth, \n" +
                "    viewportHeight: window.innerHeight,\n" +
                "    documentWidth: document.body.scrollWidth,\n" +
                "    documentHeight: document.body.scrollHeight\n" +
                "};");

        int documentWidth = toIntExact(dimensions.get("documentWidth"));
        int documentHeight = toIntExact(dimensions.get("documentHeight"));
        int viewportWidth = toIntExact(dimensions.get("viewportWidth"));
        int viewportHeight = toIntExact(dimensions.get("viewportHeight"));

        boolean hasXScroll = viewportWidth != documentWidth;
        boolean hasYScroll = viewportHeight != documentHeight;
        if (hasXScroll) {
            viewportWidth -= configuration.getScrollMarginPixels() * 2;
        }
        if (hasYScroll) {
            viewportHeight -= configuration.getScrollMarginPixels() * 2;
        }
        int scrollYTimes = (int) Math.ceil((double) documentHeight / viewportHeight);
        int scrollXTimes = (int) Math.ceil((double) documentWidth / viewportWidth);

        int imageWidth = (int) Math.ceil(documentWidth * dpr);
        int imageHeight = (int) Math.ceil(documentHeight * dpr);
        BufferedImage finalImage = new BufferedImage(imageWidth, imageHeight, TYPE_3BYTE_BGR);
        Graphics2D graphics = finalImage.createGraphics();

        int pasteX = 0;
        for (int x = 0; x < scrollXTimes; x++) {
            int pasteY = 0;
            int lastPartWidth = 0;
            for (int y = 0; y < scrollYTimes; y++) {
                webDriver.executeScript("window.scrollTo(arguments[0], arguments[1]);",
                        x * viewportWidth, y * viewportHeight);
                sleep(configuration.getScrollSleepTimeout());

                BufferedImage part = webDriver.takeScreenshot();
                part = configuration.getViewportCropper().crop(part, configuration.getImageCropper(), webDriver, dpr);
                Rectangle areaToCrop = new Rectangle(0, 0, part.getHeight(), part.getWidth());
                if (hasXScroll) {
                    if (x != 0) {
                        areaToCrop.x = (int) scrollMargin;
                        areaToCrop.width -= (int) scrollMargin;
                    }
                    if (pasteX + areaToCrop.width - (int) scrollMargin < imageWidth) {
                        areaToCrop.width -= (int) scrollMargin;
                    }
                }
                if (hasYScroll) {
                    if (y != 0) {
                        areaToCrop.y = (int) scrollMargin;
                        areaToCrop.height -= (int) scrollMargin;
                    }
                    if (pasteY + areaToCrop.height - (int) scrollMargin < imageHeight) {
                        areaToCrop.height -= (int) scrollMargin;
                    }
                }
                part = configuration.getImageCropper().crop(part, areaToCrop);

                pasteX = pasteX + part.getWidth() > imageWidth ? imageWidth - part.getWidth() : pasteX;
                pasteY = pasteY + part.getHeight() > imageHeight ? imageHeight - part.getHeight() : pasteY;
                graphics.drawImage(part, pasteX, pasteY, null);
                pasteY += part.getHeight();
                lastPartWidth = part.getWidth();
            }
            pasteX += lastPartWidth;
        }

        graphics.dispose();
        return configuration.getImageScaler().scale(finalImage, dpr);
    }

    @Override
    public String describe() {
        return "the whole page";
    }

    @SneakyThrows
    private void sleep(Duration scrollSleepTimeout) {
        Thread.sleep(scrollSleepTimeout.toMillis());
    }
}
