package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.DefaultImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfArea;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AreaScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;

    private final Rectangle rectangle = new Rectangle(2, 3, 4, 6);

    @Test
    @DisplayName("Should return screenshot of area")
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        ViewportScreenshotTest.mockDriverResponses(webDriver, image);
        BufferedImage screenshot = Screenshots.screenshotOfArea(2, 3, 6, 4)
                .take(webDriver, new DefaultImageCropper());
        BufferedImage reference = new BufferedImage(6, 4, TYPE_4BYTE_ABGR);
        Optional<ImageDiff> diff = new DefaultImageDiffer().makeDiff(screenshot, reference, emptyList());
        assertThat(diff).isEmpty();
    }

    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        Assertions.assertThat(Screenshots.screenshotOfArea(rectangle).describe())
                .isEqualTo("the area [x:2, y:3, width:6, height: 4]");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        Assertions.assertThat(Screenshots.screenshotOfArea(rectangle).as("some element").describe())
                .isEqualTo("some element (the area [x:2, y:3, width:6, height: 4])");
    }

}
