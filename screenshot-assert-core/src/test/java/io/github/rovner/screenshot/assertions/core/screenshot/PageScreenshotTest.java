package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfWholePage;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;
    @Mock
    ImageCropper cropper;
    @Mock
    PlatformScreenshoter screenshoter;
    @Mock
    ImageScaler scaler;

    @Test
    @DisplayName("Should return screenshot of viewport")
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        when(screenshoter.takeWholePageScreenshot(webDriver, cropper, scaler)).thenReturn(image);
        when(screenshoter.accept(any())).thenReturn(true);

        BufferedImage screenshot = screenshotOfWholePage()
                .take(webDriver, cropper, scaler, singletonList(screenshoter));
        assertThat(screenshot).isEqualTo(image);
    }

    @Test
    @DisplayName("Should return describe")
    void shouldDescribe() {
        assertThat(screenshotOfWholePage().describe()).isEqualTo("the whole page");
    }
}
