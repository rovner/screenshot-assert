package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.exceptions.InvalidCoordinatesException;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.Color.RED;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SoftAssertionsExtension.class)
public class DefaultImageCropperTest {

    @InjectSoftAssertions
    SoftAssertions softly;

    private final DefaultImageCropper cropper = new DefaultImageCropper();

    @ParameterizedTest(name = "[{0}, {1}, {2}, {3}]")
    @CsvSource({
            "0, 0, 11, 5",
            "0, 0, 5, 11",
            "5, 0, 6, 5",
            "11, 0, 5, 5",
            "0, 11, 5, 5"
    })
    @DisplayName("Should throw exception when crop area is outside the image")
    void shouldThrowExceptionIfAreNotInsideImage(int x, int y, int width, int height) {
        BufferedImage image = new BufferedImage(10, 10, TYPE_INT_RGB);

        assertThatThrownBy(() -> cropper.crop(image, new Rectangle(x, y, height, width)))
                .isInstanceOf(InvalidCoordinatesException.class)
                .hasMessage("Can not crop image because desired area is outside of image");
    }

    @ParameterizedTest(name = "[{0}, {1}, {2}, {3}]")
    @CsvSource({
            "0, 0, 5, 5",
            "0, 5, 5, 5",
            "5, 0, 5, 5",
            "5, 5, 5, 5",
    })
    @DisplayName("Should crop area from image")
    void shouldCropImage(int x, int y, int width, int height) throws IOException {
        BufferedImage image = generateImage(x, y, width, height);

        BufferedImage croped = cropper.crop(image, new Rectangle(x, y, height, width));

        softly.assertThat(croped.getWidth()).isEqualTo(width);
        softly.assertThat(croped.getHeight()).isEqualTo(height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                softly.assertThat(croped.getRGB(i, j)).isEqualTo(RED.getRGB());
            }
        }
    }

    @Test
    @DisplayName("Should return original image of noting to crop")
    void shouldReturnOriginalImage() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_INT_RGB);
        BufferedImage crop = cropper.crop(image, new Rectangle(0, 0, 10, 10));
        assertThat(crop).isEqualTo(image);
    }

    private BufferedImage generateImage(int x, int y, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(10, 10, TYPE_INT_RGB);
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                image.setRGB(i, j, RED.getRGB());
            }
        }
        Path path = Paths.get("build/tmp/test/")
                .resolve(this.getClass().getName())
                .resolve(String.format("shouldCropImage-%d-%d-%d-%d-original.png", x, y, width, height));
        Files.createDirectories(path.getParent());
        ImageIO.write(image, "png", path.toFile());
        return image;
    }
}
