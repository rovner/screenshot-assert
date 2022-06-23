package io.github.rovner.screenshot.assertions.core.allure;

import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.github.rovner.screenshot.assertions.core.screenshot.ScreenshotDescription;

import java.awt.image.BufferedImage;

/**
 * Handles comparison result representation in allure report.
 */
public interface AllureListener {

    /**
     * Called when diff is detected
     *
     * @param diff diff data
     */
    void handleDiff(ImageDiff diff);

    /**
     * Called when io.github.rovner.screenshot.assert.is.update.reference.image=true
     *
     * @param diff diff data
     */
    void handleDiffUpdated(ImageDiff diff);

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
     * Returns name of step to be added in allure report.
     *
     * @param description screenshot description
     * @param id          reference screenshot id
     * @return name of the step
     */
    default String getCompareStepName(ScreenshotDescription description, String id) {
        return String.format("Assert that %s is equal to reference with id '%s'", description.describe(), id);
    }
}
