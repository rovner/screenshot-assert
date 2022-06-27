package io.github.rovner.screenshot.assertions.core.scaler;

import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultImageScalerTest {

    @Mock
    RemoteWebDriver webDriver;
    ImageScaler scaler = new DefaultImageScaler();
    BufferedImage image = new BufferedImage(20, 30, TYPE_INT_RGB);

    @Test
    @DisplayName("Should scale image")
    void shouldScale() {
        BufferedImage scaled = scaler.scale(image, 2.0);
        BufferedImage expected = new BufferedImage(10, 15, TYPE_INT_RGB);
        assertThat(new DefaultImageDiffer().makeDiff(scaled, expected, emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Should not scale image")
    void shouldNotScale() {
        BufferedImage scaled = scaler.scale(image, 1);
        assertThat(scaled).isEqualTo(image);
    }
}
