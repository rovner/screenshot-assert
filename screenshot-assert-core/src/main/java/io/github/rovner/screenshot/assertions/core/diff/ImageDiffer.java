package io.github.rovner.screenshot.assertions.core.diff;

import io.github.rovner.screenshot.assertions.core.ignoring.Ignoring;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Image comparator
 */
public interface ImageDiffer {
    /**
     * Compares to image and returns comparison result if images are different.
     * Returns empty if there are no differences or all of them are ignored.
     *
     * @param actual    screenshot to compare
     * @param reference screenshot to compare with
     * @param ignoredAreas  areas that should be ignored during comparison
     * @param ignoredHashes  areas that should be ignored during comparison
     * @return diff data or empty
     */
    Optional<ImageDiff> makeDiff(BufferedImage actual, BufferedImage reference,
                                 Set<Rectangle> ignoredAreas, Set<Integer> ignoredHashes);
}
