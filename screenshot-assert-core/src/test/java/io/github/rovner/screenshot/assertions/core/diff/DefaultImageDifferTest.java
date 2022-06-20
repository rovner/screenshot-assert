package io.github.rovner.screenshot.assertions.core.diff;

import io.github.rovner.screenshot.assertions.core.ignoring.Ignoring;
import io.github.rovner.screenshot.assertions.core.ignoring.Ignorings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import static java.awt.Color.*;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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
    void shouldBeEqualForTheSameImages() {
        assertThat(differ.makeDiff(actual, reference, emptyList())).isEmpty();
    }

    @Test
    void shouldBeEqualForImagesWithDistortionLessThanDefault() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 10);

        assertThat(differ.makeDiff(actual, reference, emptyList())).isEmpty();
    }

    @Test
    void shouldBeEqualForImagesWithDistortionLessThanCustom() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 1);

        differ.withColorDistortion(1);
        assertThat(differ.makeDiff(actual, reference, emptyList())).isEmpty();
    }

    @Test
    void shouldNotBeEqualForNotEqualImagesWithZeroDistortion() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 1);
        differ.withColorDistortion(0);
        assertThat(differ.makeDiff(actual, reference, emptyList())).isNotEmpty();
    }

    @Test
    void shouldBeEqualIfDiffIsLessThanTriggerSize() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);

        differ.withDiffSizeTrigger(1);
        assertThat(differ.makeDiff(actual, reference, emptyList())).isEmpty();
    }

    @Test
    void shouldBeEqualIfIgnoredByHash() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);

        Assertions.assertThat(differ.makeDiff(actual, reference, singletonList(Ignorings.hash(961)))).isEmpty();
    }

    @Test
    void shouldBeEqualIfIgnoredByCoordinate() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 12);
        assertThat(differ.makeDiff(actual, reference, singletonList(Ignorings.area(0, 0, 1, 1)))).isEmpty();
    }

    @Test
    void shouldBeNotEqual() {
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 11);
        List<Integer> hashes = asList(1, 2, 3);
        Rectangle rect1 = new Rectangle(1, 1, 2, 2);
        Rectangle rect2 = new Rectangle(0, 3, 2, 2);
        List<Ignoring> ignorings = asList(
                Ignorings.hashes(hashes),
                Ignorings.areas(rect1, rect2)
        );
        Optional<ImageDiff> diff = differ.makeDiff(actual, reference, ignorings);
        Assertions.assertThat(diff).isNotEmpty();
        assertThat(diff.get().getDiff().getRGB(0, 0)).isEqualTo(RED.getRGB());
    }

    @Test
    void shouldChangeDiffColor() {
        actual.setRGB(1, 1, BLUE.getRGB());
        reference.setRGB(1, 1, BLUE.getRGB());
        actual.setRGB(0, 0, RED.getRGB());
        reference.setRGB(0, 0, RED.getRGB() + 11);
        Optional<ImageDiff> diff = differ
                .withDiffColor(ORANGE)
                .makeDiff(actual, reference, emptyList());
        Assertions.assertThat(diff).isNotEmpty();
        assertThat(diff.get().getDiff().getRGB(0, 0)).isEqualTo(ORANGE.getRGB());
        assertThat(diff.get().getDiff().getRGB(1, 1)).isEqualTo(BLUE.getRGB());
    }

    @ParameterizedTest
    @CsvSource({
            "11, 10, 10, 10",
            "10, 11, 10, 10",
            "10, 10, 11, 10",
            "10, 10, 10, 11",
    })
    void shouldBeNotEqualIfDifferentSize(int actualWidth, int actualHeight, int referenceWidth, int referenceHeight) {
        BufferedImage actual1 = new BufferedImage(actualWidth, actualHeight, TYPE_INT_RGB);
        BufferedImage reference1 = new BufferedImage(referenceWidth, referenceHeight, TYPE_INT_RGB);
        Optional<ImageDiff> diff = differ
                .makeDiff(actual1, reference1, emptyList());
        Assertions.assertThat(diff).isNotEmpty();
    }
}
