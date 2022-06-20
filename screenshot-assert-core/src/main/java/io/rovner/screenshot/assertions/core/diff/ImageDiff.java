package io.rovner.screenshot.assertions.core.diff;

import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * Diff data.
 */
@Data
@Builder
public class ImageDiff {
    private BufferedImage actual;
    private BufferedImage reference;
    private BufferedImage diff;
    private Integer diffHash;
    private Set<Integer> ignoredHashes;
    private Set<Rectangle> ignoredAreas;
}
