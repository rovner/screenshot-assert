package io.rovner.screenshot.assertions.core.diff;

import io.rovner.screenshot.assertions.core.ignoring.Ignoring;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;

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
     * @param ignoring  areas, elements or hashes that should be ignored during comparison
     * @return diff data or empty
     */
    Optional<ImageDiff> makeDiff(BufferedImage actual, BufferedImage reference, Collection<Ignoring> ignoring);
}
