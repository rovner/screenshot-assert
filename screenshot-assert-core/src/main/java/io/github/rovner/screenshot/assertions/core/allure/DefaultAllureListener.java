package io.github.rovner.screenshot.assertions.core.allure;

import com.google.gson.Gson;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toBase64;
import static io.github.rovner.screenshot.assertions.core.ImageUtils.toByteArray;
import static java.util.stream.Collectors.joining;

/**
 * Default allure listener that uses
 * <a href="https://github.com/allure-framework/allure2/tree/master/plugins/screen-diff-plugin">default allure plugin</a>
 * for diff representation.
 */
public class DefaultAllureListener implements AllureListener {

    public static final Gson GSON = new Gson();
    public static final String DATA_IMAGE_PNG_BASE_64 = "data:image/png;base64,";

    @Override
    public void handleDiff(ImageDiff diff) {
        Allure.label("testType", "screenshotDiff");
        attachScreenDiff(diff);
        attachDiffInfo(diff);
    }

    @Override
    public void handleDiffUpdated(ImageDiff diff) {
        this.handleDiff(diff);
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

    @Override
    public void handleContextScreenshot(BufferedImage screenshot) {
        attachScreenshot("screenshot diff context", screenshot);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "{name}", type = "image/png", fileExtension = "png")
    private byte[] attachScreenshot(@SuppressWarnings({"unused", "SameParameterValue"}) String name, BufferedImage image) {
        return toByteArray(image);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "screenshot diff info", type = "text/plain", fileExtension = "txt")
    private String attachDiffInfo(ImageDiff diff) {
        StringBuilder sb = new StringBuilder()
                .append("hash code: ")
                .append(diff.getDiffHash())
                .append("\n");
        if (!diff.getIgnoredHashes().isEmpty()) {
            sb.append("ignored hashes: ")
                    .append(diff.getIgnoredHashes().stream().map(String::valueOf).collect(joining(",")))
                    .append("\n");

        }
        if (!diff.getIgnoredHashes().isEmpty()) {
            sb.append("ignored areas: ")
                    .append(diff.getIgnoredAreas().stream().map(this::rectangleToString).collect(joining(",")))
                    .append("\n");

        }
        return sb.toString();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "screenshot diff", type = "application/vnd.allure.image.diff", fileExtension = "json")
    private String attachScreenDiff(ImageDiff diff) {
        return GSON.toJson(ScreenDiff.builder()
                .actual(DATA_IMAGE_PNG_BASE_64 + toBase64(diff.getActual()))
                .expected(DATA_IMAGE_PNG_BASE_64 + toBase64(diff.getReference()))
                .diff(DATA_IMAGE_PNG_BASE_64 + toBase64(diff.getDiff()))
                .build());
    }

    @Data
    @Builder
    public static class ScreenDiff {
        private String expected;
        private String actual;
        private String diff;
    }
}
