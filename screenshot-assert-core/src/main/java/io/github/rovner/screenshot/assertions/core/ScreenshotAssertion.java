package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import io.github.rovner.screenshot.assertions.core.exceptions.ScreenshotAssertionError;
import io.github.rovner.screenshot.assertions.core.ignoring.Ignoring;
import io.github.rovner.screenshot.assertions.core.ignoring.WebDriverInit;
import io.github.rovner.screenshot.assertions.core.screenshot.KeepContextScreenshot;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * Assertion that:
 * <ul>
 *     <li>Takes screenshot of page/area/element.</li>
 *     <li>Compares it to reference.</li>
 * </ul>
 */
@Slf4j
public class ScreenshotAssertion {
    private final Set<Ignoring> ignorings = new HashSet<>();
    private final WebDriver webDriver;
    private final ScreenshotAssertionConfiguration configuration;
    private final ScreenshotAssertionProperties properties;
    private final Screenshot screenshot;

    public ScreenshotAssertion(WebDriver webDriver,
                               ScreenshotAssertionConfiguration configuration,
                               ScreenshotAssertionProperties properties,
                               Screenshot screenshot) {
        this.webDriver = webDriver;
        this.configuration = configuration;
        this.properties = properties;
        this.screenshot = screenshot;
    }

    /**
     * Ignores some diff (area, element, hash, etc.).
     *
     * @param ignoring some difference that will be ignored.
     * @return self.
     */
    public ScreenshotAssertion ignoring(Ignoring ignoring) {
        ignorings.add(ignoring);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     *
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssertion ignoring(Collection<Ignoring> ignorings) {
        this.ignorings.addAll(ignorings);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     *
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssertion ignoring(Ignoring... ignorings) {
        this.ignorings.addAll(Arrays.asList(ignorings));
        return this;
    }

    /**
     * Takes screenshot and compares it to reference.
     *
     * @param id reference id
     */
    public void isEqualToReferenceId(String id) {
        String stepName = configuration.getAllureListener().getCompareStepName(screenshot, id);

        if (configuration.isSoft() || properties.isSoft()) {
            try {
                takeScreenshotAndCompare(id, stepName);
            } catch (ScreenshotAssertionError | NoReferenceException e) {
                configuration.getSoftExceptionCollector().add(e);
            }
        } else {
            takeScreenshotAndCompare(id, stepName);
        }
    }

    @Step("{stepName}")
    private void takeScreenshotAndCompare(String id, @SuppressWarnings("unused") String stepName) {
        initWebDriver();
        BufferedImage actual = screenshot.take(new WebDriverWrapper(webDriver), configuration.getScreenshotConfiguration());
        BufferedImage reference;
        try {
            reference = configuration.getReferenceStorage().read(id);
        } catch (IOException e) {
            configuration.getAllureListener().handleNoReference(actual);
            if (properties.isSaveReferenceImageWhenMissing()) {
                configuration.getReferenceStorage().write(id, actual);
            }
            throw new NoReferenceException(String.format("No reference image %s, " +
                    "current screenshot saved as reference", configuration.getReferenceStorage().describe(id)), e);
        }
        Optional<ImageDiff> diff = configuration.getImageDiffer().makeDiff(actual, reference, ignorings);
        if (diff.isPresent()) {
            if (properties.isUpdateReferenceImage()) {
                configuration.getReferenceStorage().write(id, actual);
                configuration.getAllureListener().handleDiffUpdated(diff.get());
            } else {
                configuration.getAllureListener().handleDiff(diff.get());
                if (screenshot instanceof KeepContextScreenshot) {
                    BufferedImage context = ((KeepContextScreenshot) screenshot)
                            .getContextScreenshot(configuration.getContextMarkColor());
                    configuration.getAllureListener().handleContextScreenshot(context);
                }
                throw new ScreenshotAssertionError(String.format("Expected screenshot of %s to be equal to " +
                        "reference %s, but is was not", screenshot.describe(), id));
            }
        } else {
            configuration.getAllureListener().handleNoDiff(actual);
        }
    }

    private void initWebDriver() {
        ignorings.stream()
                .filter(ignoring -> ignoring instanceof WebDriverInit)
                .map(ignoring -> (WebDriverInit) ignoring)
                .forEach(ignoring -> ignoring.initWebDriver(webDriver));
    }
}
