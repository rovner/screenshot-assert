package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.*;
import static java.awt.Color.RED;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageUtilsTest {

    BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should convert bytes <-> buffered image")
    void shouldConvert() {
        byte[] bytes = toByteArray(image);
        assertThat(bytes).hasSize(71);
        BufferedImage actual = toBufferedImage(bytes);
        assertThat(new DefaultImageDiffer().makeDiff(actual, image, emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Should convert to base 64")
    void shouldConvertToBase64() {
        assertThat(toBase64(image))
                .isEqualTo("iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAADklEQVR42mNgGAWDEwAAAZoAAQDqGN4AAAAASUVORK5CYII=");
    }

    @Test
    @DisplayName("Should draw rectangle")
    void shouldDrawRectangle() {
        BufferedImage newImage = drawRectangle(image, new Rectangle(1, 1, 2, 3), RED);
        //top
        assertThat(newImage.getRGB(1, 1)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(2, 1)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(3, 1)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(4, 1)).isEqualTo(RED.getRGB());

        //bottom
        assertThat(newImage.getRGB(1, 3)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(2, 3)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(3, 3)).isEqualTo(RED.getRGB());
        assertThat(newImage.getRGB(4, 3)).isEqualTo(RED.getRGB());

        //left
        assertThat(newImage.getRGB(1, 2)).isEqualTo(RED.getRGB());

        //right
        assertThat(newImage.getRGB(4, 2)).isEqualTo(RED.getRGB());
    }
}
