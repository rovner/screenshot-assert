package com.github.rovner.screenshot.assertions.core;

import com.github.rovner.screenshot.assertions.core.allure.AllureListener;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import com.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import com.github.rovner.screenshot.assertions.core.exceptions.ScreenshotAssertionError;
import com.github.rovner.screenshot.assertions.core.ignoring.Ignoring;
import com.github.rovner.screenshot.assertions.core.ignoring.WebDriverInit;
import com.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static io.qameta.allure.Allure.step;
import static java.nio.file.Files.createDirectories;

/**
 * Assertion that:
 * <ul>
 *     <li>Takes screenshot of page/area/element.</li>
 *     <li>Compares it to reference.</li>
 * </ul>
 */
@Slf4j
@Builder
public class ScreenshotAssert {

    private final WebDriver webDriver;
    private final Screenshot screenshot;
    private final Path references;
    private final ImageDiffer imageDiffer;
    private final Set<Ignoring> ignorings = new HashSet<>();
    private final AllureListener allureListener;

    /**
     * Ignores some diff (area, element, hash, etc.).
     * @param ignoring some difference that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Ignoring ignoring) {
        ignorings.add(ignoring);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Collection<Ignoring> ignorings) {
        this.ignorings.addAll(ignorings);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Ignoring... ignorings) {
        this.ignorings.addAll(Arrays.asList(ignorings));
        return this;
    }

    /**
     * Takse screenshot and compares it to reference.
     * @param id reference id
     */
    public void isEqualToReferenceId(String id) {
        try {
            Runnable compareOperation = () -> {
                initWebDriver();
                Path referencePath = references.resolve(id + ".png");
                BufferedImage actual = screenshot.take(webDriver);
                BufferedImage reference = readImage(referencePath)
                        .orElseGet(() -> {
                            allureListener.handleNoReference(actual);
                            saveActualAsReference(referencePath, actual);
                            throw new NoReferenceException(String.format("No reference image at path %s, " +
                                    "current screenshot saved as reference", referencePath));
                        });
                Optional<ImageDiff> diff = imageDiffer.makeDiff(actual, reference, ignorings);
                if (diff.isPresent()) {
                    throw new ScreenshotAssertionError(String.format("Expected screenshot of %s to be equal to " +
                            "reference %s, but is was not", screenshot.describe(), id), diff.get());
                } else {
                    allureListener.handleNoDiff(actual);
                }
            };

            Optional<String> compareStepName = allureListener.getCompareStepName(screenshot, id);
            if (compareStepName.isPresent()) {
                step(compareStepName.get(), compareOperation::run);
            } else {
                compareOperation.run();
            }

        } catch (ScreenshotAssertionError error) {
            allureListener.handleDiff(error.getDiff());
            throw error;
        }
    }

    private void initWebDriver() {
        ignorings.stream()
                .filter(ignoring -> ignoring instanceof WebDriverInit)
                .map(ignoring -> (WebDriverInit) ignoring)
                .forEach(ignoring -> ignoring.initWebDriver(webDriver));
    }

    private void saveActualAsReference(Path referencePath, BufferedImage actual) {
        try {
            createDirectories(referencePath.getParent());
            ImageIO.write(actual, "png", referencePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to save reference file to path %s", referencePath));
        }
    }

    private static Optional<BufferedImage> readImage(Path path) {
        try {
            return Optional.of(ImageIO.read(path.toFile()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
