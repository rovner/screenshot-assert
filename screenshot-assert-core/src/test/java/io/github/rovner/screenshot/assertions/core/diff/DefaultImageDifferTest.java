package io.github.rovner.screenshot.assertions.core.diff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.awt.Color.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultImageDifferTest {

    private BufferedImage actual;
    private BufferedImage reference;
    private DefaultImageDiffer differ;

    @BeforeEach
    void beforeEach() {
        actual = new BufferedImage(10, 10, TYPE_INT_RGB);
        reference = new BufferedImage(10, 10, TYPE_INT_RGB);
        differ = new DefaultImageDiffer();
    }

    @Test
    @DisplayName("Should be empty for equal images on byte array level")
    void shouldBeEqualForTheSameImages() {
        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isEmpty();
    }

    @Test
    @DisplayName("Should return dif for images with different color models")
    void shouldBeEqualForImagesWithDifferentColorModels() {
        actual.setRGB(0, 0, RED.getRGB());
        reference = new BufferedImage(10, 10, TYPE_INT_ARGB);

        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isNotEmpty();
    }

    @Test
    @DisplayName("Should be empty for images with distortion in channel less than default distortion")
    void shouldBeEqualForImagesWithDistortionRedLessThanDefault() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 10);

        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isEmpty();
    }

    @Test
    @DisplayName("Should be empty for images with distortion in channel less than custom distortion")
    void shouldBeEqualForImagesWithDistortionLessThanCustom() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 1);

        differ.setColorDistortion(1);
        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isEmpty();
    }

    @Test
    @DisplayName("Should be empty for images with distortion in channel less than zero distortion")
    void shouldNotBeEqualForNotEqualImagesWithZeroDistortion() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 1);
        differ.setColorDistortion(0);
        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isNotEmpty();
    }

    @Test
    @DisplayName("Should be empty for images with diff points less than configured diff trigger size")
    void shouldBeEqualIfDiffIsLessThanTriggerSize() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);

        differ.setDiffSizeTrigger(1);
        assertThat(differ.makeDiff(actual, reference, emptySet(), emptySet())).isEmpty();
    }

    @Test
    @DisplayName("Should be empty for images images with ignore by hash code")
    void shouldBeEqualIfIgnoredByHash() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);

        assertThat(differ.makeDiff(actual, reference, emptySet(), singletonMap(961, null).keySet())).isEmpty();
    }

    @Test
    @DisplayName("Should be empty for images images with ignore by coordinates")
    void shouldBeEqualIfIgnoredByCoordinate() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);
        assertThat(differ.makeDiff(actual, reference, singletonMap(new Rectangle(0, 0, 1, 1), null).keySet(), emptySet())).isEmpty();
    }

    @SuppressWarnings("unused")
    @ParameterizedTest(name = " {7}")
    @CsvSource({
            "225, 0, 0, 255, 0, 0, red",
            "0, 225, 0, 0, 255, 0, green",
            "0, 0, 225, 0, 0, 255, blue"
    })
    @DisplayName("Should return diff if there is diff in channel")
    void shouldBeNotEqual1(int red1, int green1, int blue1, int red2, int green2, int blue2, String name) {
        Color color1 = new Color(red1, green1, blue1);
        Color color2 = new Color(red2, green2, blue2);
        actual.setRGB(0, 0, color1.getRGB());
        reference.setRGB(0, 0, color2.getRGB());
        Set<Integer> hashes = new HashSet<>(asList(1, 2, 3));
        Rectangle rect1 = new Rectangle(1, 1, 2, 2);
        Rectangle rect2 = new Rectangle(0, 3, 2, 2);
        HashSet<Rectangle> rects = new HashSet<>(asList(rect1, rect2));
        Optional<ImageDiff> diff = differ.makeDiff(actual, reference, rects, hashes);
        assertThat(diff).isNotEmpty();
        assertThat(diff.get().getDiff().getRGB(0, 0)).isEqualTo(RED.getRGB());
    }

    @Test
    @DisplayName("Should return diff with custom diff color")
    void shouldChangeDiffColor() {
        actual.setRGB(1, 1, BLUE.getRGB());
        reference.setRGB(1, 1, BLUE.getRGB());
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 11);
        Optional<ImageDiff> diff = differ
                .setDiffColor(ORANGE)
                .makeDiff(actual, reference, emptySet(), emptySet());
        assertThat(diff).isNotEmpty();
        assertThat(diff.get().getDiff().getRGB(0, 0)).isEqualTo(ORANGE.getRGB());
        assertThat(diff.get().getDiff().getRGB(1, 1)).isEqualTo(BLUE.getRGB());
    }

    @ParameterizedTest(name = "[{0} ,{1} ,{2} ,{3}]")
    @CsvSource({
            "11, 10, 10, 10",
            "10, 11, 10, 10",
            "10, 10, 11, 10",
            "10, 10, 10, 11",
    })
    @DisplayName("Should return difference if images have different sizes")
    void shouldBeNotEqualIfDifferentSize(int actualWidth, int actualHeight, int referenceWidth, int referenceHeight) {
        BufferedImage actual1 = new BufferedImage(actualWidth, actualHeight, TYPE_INT_RGB);
        BufferedImage reference1 = new BufferedImage(referenceWidth, referenceHeight, TYPE_INT_RGB);
        Optional<ImageDiff> diff = differ
                .makeDiff(actual1, reference1, emptySet(), emptySet());
        assertThat(diff).isNotEmpty();
    }
}
