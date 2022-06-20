package io.rovner.screenshot.assertions.core.allure;

import io.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.rovner.screenshot.assertions.core.screenshot.ScreenshotDescription;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Handles comparison result representation in allure report.
 */
public interface AllureListener {

    /**
     * Called when diff is detected
     *
     * @param diff data
     */
    void handleDiff(ImageDiff diff);

    /**
     * Called when no reference image is found
     *
     * @param actual screenshot from browser
     */
    void handleNoReference(BufferedImage actual);

    /**
     * Called when there are no difference between actual and reference screenshots
     *
     * @param actual screenshot from browser
     */
    void handleNoDiff(BufferedImage actual);

    /**
     * Returns name of step to bew added in allure report. Empty optional means that no step will be added in report.
     *
     * @param description screenshot description
     * @param id          reference screenshot id
     * @return name of the step or empty
     */
    default Optional<String> getCompareStepName(ScreenshotDescription description, String id) {
        return Optional.of(String.format("Assert that %s is equal to reference with id '%s'", description.describe(), id));
    }
}
