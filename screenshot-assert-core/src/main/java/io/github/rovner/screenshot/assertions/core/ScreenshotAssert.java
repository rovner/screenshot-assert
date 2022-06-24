package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import io.github.rovner.screenshot.assertions.core.exceptions.ScreenshotAssertionError;
import io.github.rovner.screenshot.assertions.core.ignoring.Ignoring;
import io.github.rovner.screenshot.assertions.core.ignoring.WebDriverInit;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import io.github.rovner.screenshot.assertions.core.soft.SoftExceptionCollector;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

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
public class ScreenshotAssert {
    private final WebDriver webDriver;
    private final ReferenceStorage referenceStorage;
    private final ImageDiffer imageDiffer;
    private final ImageCropper imageCropper;
    private final ImageScaler imageScaler;
    private final List<PlatformScreenshoter> screenshoters;
    private final AllureListener allureListener;
    private final Set<Ignoring> ignorings = new HashSet<>();
    private final boolean isSoft;
    private final SoftExceptionCollector softExceptionCollector;
    private final ScreenshotAssertConfig cfg;
    private final Screenshot screenshot;

    public ScreenshotAssert(WebDriver webDriver,
                            ReferenceStorage referenceStorage,
                            ImageDiffer imageDiffer,
                            ImageCropper imageCropper,
                            ImageScaler imageScaler,
                            List<PlatformScreenshoter> screenshoters,
                            AllureListener allureListener,
                            boolean isSoft,
                            SoftExceptionCollector softExceptionCollector,
                            ScreenshotAssertConfig cfg,
                            Screenshot screenshot) {
        this.webDriver = webDriver;
        this.referenceStorage = referenceStorage;
        this.imageDiffer = imageDiffer;
        this.imageCropper = imageCropper;
        this.imageScaler = imageScaler;
        this.screenshoters = screenshoters;
        this.allureListener = allureListener;
        this.isSoft = isSoft;
        this.softExceptionCollector = softExceptionCollector;
        this.cfg = cfg;
        this.screenshot = screenshot;
    }

    /**
     * Ignores some diff (area, element, hash, etc.).
     *
     * @param ignoring some difference that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Ignoring ignoring) {
        ignorings.add(ignoring);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     *
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Collection<Ignoring> ignorings) {
        this.ignorings.addAll(ignorings);
        return this;
    }

    /**
     * Ignores some diffs (areas, elements, hashes, etc.).
     *
     * @param ignorings some differences that will be ignored.
     * @return self.
     */
    public ScreenshotAssert ignoring(Ignoring... ignorings) {
        this.ignorings.addAll(Arrays.asList(ignorings));
        return this;
    }

    /**
     * Takes screenshot and compares it to reference.
     *
     * @param id reference id
     */
    public void isEqualToReferenceId(String id) {
        String stepName = allureListener.getCompareStepName(screenshot, id);

        if (isSoft || cfg.isSoft()) {
            try {
                takeScreenshotAndCompare(id, stepName);
            } catch (ScreenshotAssertionError | NoReferenceException e) {
                softExceptionCollector.add(e);
            }
        } else {
            takeScreenshotAndCompare(id, stepName);
        }
    }

    @Step("{stepName}")
    private void takeScreenshotAndCompare(String id, @SuppressWarnings("unused") String stepName) {
        initWebDriver();
        BufferedImage actual = screenshot.take(webDriver, imageCropper, imageScaler, screenshoters);
        BufferedImage reference;
        try {
            reference = referenceStorage.read(id);
        } catch (IOException e) {
            allureListener.handleNoReference(actual);
            if (cfg.isSaveReferenceImageWhenMissing()) {
                referenceStorage.write(id, actual);
            }
            throw new NoReferenceException(String.format("No reference image %s, " +
                    "current screenshot saved as reference", referenceStorage.describe(id)), e);
        }
        Optional<ImageDiff> diff = imageDiffer.makeDiff(actual, reference, ignorings);
        if (diff.isPresent()) {
            if (cfg.isUpdateReferenceImage()) {
                referenceStorage.write(id, actual);
                allureListener.handleDiffUpdated(diff.get());
            } else {
                allureListener.handleDiff(diff.get());
                throw new ScreenshotAssertionError(String.format("Expected screenshot of %s to be equal to " +
                        "reference %s, but is was not", screenshot.describe(), id));
            }
        } else {
            allureListener.handleNoDiff(actual);
        }
    }

    private void initWebDriver() {
        ignorings.stream()
                .filter(ignoring -> ignoring instanceof WebDriverInit)
                .map(ignoring -> (WebDriverInit) ignoring)
                .forEach(ignoring -> ignoring.initWebDriver(webDriver));
    }
}
