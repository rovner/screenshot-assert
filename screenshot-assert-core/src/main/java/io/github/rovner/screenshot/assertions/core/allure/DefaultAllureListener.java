package io.github.rovner.screenshot.assertions.core.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.github.rovner.screenshot.assertions.core.ImageUtils;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

import static java.util.stream.Collectors.joining;

/**
 * Default allure listener that uses
 * <a href="https://github.com/allure-framework/allure2/tree/master/plugins/screen-diff-plugin">default allure plugin</a>
 * for diff representation.
 */
public class DefaultAllureListener implements AllureListener {

    @Override
    public void handleDiff(ImageDiff diff) {
        Allure.label("testType", "screenshotDiff");
        attachScreenshot("diff", diff.getDiff());
        attachScreenshot("actual", diff.getActual());
        attachScreenshot("expected", diff.getReference());
        //noinspection StringBufferReplaceableByString
        attachText("diff info", new StringBuilder()
                .append("hash code: ")
                .append(diff.getDiffHash())
                .append("\n")
                .append("ignored hashes: ")
                .append(diff.getIgnoredHashes().stream().map(String::valueOf).collect(joining(",")))
                .append("\n")
                .append("ignored areas: ")
                .append(diff.getIgnoredAreas().stream().map(this::rectangleToString).collect(joining(",")))
                .append("\n")
                .toString());
    }

    private String rectangleToString(Rectangle rectangle) {
        return String.format("[x: %s, y: %s, width: %s, height: %s]",
                rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public void handleNoReference(BufferedImage actual) {
        attachScreenshot("actual", actual);
    }

    @Override
    public void handleNoDiff(BufferedImage actual) {
        attachScreenshot("actual", actual);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "{name}", type = "image/png", fileExtension = "png")
    private byte[] attachScreenshot(@SuppressWarnings("unused") String name, BufferedImage image) {
        return ImageUtils.toByteArray(image);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "{name}", type = "text/plain", fileExtension = "txt")
    private String attachText(@SuppressWarnings({"unused", "SameParameterValue"}) String name, String text) {
        return text;
    }
}
