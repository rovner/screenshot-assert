package io.rovner.screenshot.assertions.core.diff;

import io.rovner.screenshot.assertions.core.ignoring.AreaIgnoring;
import io.rovner.screenshot.assertions.core.ignoring.HashIgnoring;
import io.rovner.screenshot.assertions.core.ignoring.Ignoring;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.awt.Color.RED;
import static java.lang.Integer.MAX_VALUE;

/**
 * Default image comparator that:
 * <pre>
 * - Compares byte array of buffered images.
 * - Compares images by pixels if bytes of buffered images are different.
 * - Pixels are compared by distortion in each color channel. Max distortion could be configured via {@link #withColorDistortion(int)}
 * - Maximum different pixels could be configured via {@link #withDiffSizeTrigger(int)}
 * - Diff pixel color on marked diff could be configured via {@link #withDiffColor(Color)}
 * </pre>
 */
public class DefaultImageDiffer implements ImageDiffer {
    private int colorDistortion = 10;
    private int diffSizeTrigger = 0;
    private Color diffColor = RED;

    /**
     * Sets maximum color distortion in each color channel. Default is 10.
     *
     * @param distortion maximum color distortion.
     * @return self
     */
    public DefaultImageDiffer withColorDistortion(int distortion) {
        this.colorDistortion = distortion;
        return this;
    }

    /**
     * Sets max possible different pixels. Default is 0.
     *
     * @param diffSizeTrigger max possible different pixels.
     * @return self
     */
    public DefaultImageDiffer withDiffSizeTrigger(int diffSizeTrigger) {
        this.diffSizeTrigger = diffSizeTrigger;
        return this;
    }

    /**
     * Sets color that will be used to mark diff points on diff image. Default is red.
     *
     * @param diffColor diff point color.
     * @return self
     */
    public DefaultImageDiffer withDiffColor(Color diffColor) {
        this.diffColor = diffColor;
        return this;
    }

    @Override
    public Optional<ImageDiff> makeDiff(BufferedImage actual, BufferedImage reference, Collection<Ignoring> ignorings) {
        if (areImagesEqual(actual, reference)) {
            return Optional.empty();
        }
        Set<Rectangle> ignoredAreas = AreaIgnoring.getIgnoredAreas(ignorings);
        Set<Point> diffPoints = findDiffPoints(actual, reference, ignoredAreas);
        if (diffPoints.size() <= diffSizeTrigger) {
            return Optional.empty();
        }
        int diffHashCode = countDiffPointsHashCode(diffPoints);
        Set<Integer> ignoredHashes = HashIgnoring.getIgnoredHashes(ignorings);
        if (ignoredHashes.contains(diffHashCode)) {
            return Optional.empty();
        }
        return Optional.of(ImageDiff.builder()
                .actual(actual)
                .reference(reference)
                .diff(markDiffImage(actual, reference, diffPoints))
                .diffHash(diffHashCode)
                .ignoredAreas(ignoredAreas)
                .ignoredHashes(ignoredHashes)
                .build());
    }

    private BufferedImage markDiffImage(BufferedImage actual, BufferedImage reference, Set<Point> diffPoints) {
        int maxWidth = Math.max(actual.getWidth(), reference.getWidth());
        int maxHeight = Math.max(actual.getHeight(), reference.getHeight());
        BufferedImage diffImage = new BufferedImage(maxWidth, maxHeight, actual.getType());
        Graphics graphics = diffImage.getGraphics();
        graphics.drawImage(actual, 0, 0, null);
        graphics.drawImage(reference, 0, 0, null);
        graphics.dispose();
        int rgb = diffColor.getRGB();
        for (Point dot : diffPoints) {
            diffImage.setRGB(dot.x, dot.y, rgb);
        }
        return diffImage;
    }


    private static int countDiffPointsHashCode(Set<Point> diffPoints) {
        int xReference = MAX_VALUE;
        int yReference = MAX_VALUE;
        for (Point point : diffPoints) {
            xReference = Math.min(xReference, point.x);
            yReference = Math.min(yReference, point.y);
        }
        Set<Point> relativePoints = new HashSet<>();
        for (Point point : diffPoints) {
            relativePoints.add(new Point(point.x - xReference, point.y - yReference));
        }

        return relativePoints.hashCode();
    }

    private Set<Point> findDiffPoints(BufferedImage actual, BufferedImage reference, Collection<Rectangle> ignoredAreas) {
        int maxWidth = Math.max(actual.getWidth(), reference.getWidth());
        int maxHeight = Math.max(actual.getHeight(), reference.getHeight());
        Set<Point> diffPoints = new HashSet<>();
        for (int x = 0; x < maxWidth; x++) {
            for (int y = 0; y < maxHeight; y++) {
                Point point = new Point(x, y);
                if (ignoredAreas.stream().anyMatch(area -> isInsideRectangle(area, point))) {
                    continue;
                }
                if (x >= actual.getWidth() || x >= reference.getWidth() || y >= actual.getHeight() || y >= reference.getHeight()) {
                    diffPoints.add(point);
                    continue;
                }
                if (hasDiffInChannel(actual.getRGB(x, y), reference.getRGB(x, y))) {
                    diffPoints.add(point);
                }
            }
        }
        return diffPoints;
    }

    private boolean hasDiffInChannel(int rgb1, int rgb2) {
        if (colorDistortion == 0) {
            return rgb1 != rgb2;
        }
        int red1 = (rgb1 & 0x00FF0000) >> 16;
        int green1 = (rgb1 & 0x0000FF00) >> 8;
        int blue1 = (rgb1 & 0x000000FF);
        int red2 = (rgb2 & 0x00FF0000) >> 16;
        int green2 = (rgb2 & 0x0000FF00) >> 8;
        int blue2 = (rgb2 & 0x000000FF);
        return Math.abs(red1 - red2) > colorDistortion
                || Math.abs(green1 - green2) > colorDistortion
                || Math.abs(blue1 - blue2) > colorDistortion;
    }

    private static boolean areImagesEqual(BufferedImage expected, BufferedImage actual) {
        return expected.getHeight() == actual.getHeight()
                && expected.getWidth() == actual.getWidth()
                && actual.getColorModel().equals(expected.getColorModel())
                && areImagesBuffersEqual(expected.getRaster().getDataBuffer(), actual.getRaster().getDataBuffer());
    }

    private static boolean areImagesBuffersEqual(DataBuffer expected, DataBuffer actual) {
        return actual.getDataType() == expected.getDataType()
                && actual.getNumBanks() == expected.getNumBanks()
                && actual.getSize() == expected.getSize()
                && areImagesBytesEqual(actual, expected);
    }

    private static boolean areImagesBytesEqual(DataBuffer expected, DataBuffer actual) {
        for (int bank = 0; bank < expected.getNumBanks(); bank++) {
            for (int i = 0; i < expected.getSize(); i++) {
                if (expected.getElem(bank, i) != actual.getElem(bank, i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isInsideRectangle(Rectangle rectangle, Point point) {
        return new java.awt.Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
                .contains(new java.awt.Point(point.x, point.y));
    }
}
