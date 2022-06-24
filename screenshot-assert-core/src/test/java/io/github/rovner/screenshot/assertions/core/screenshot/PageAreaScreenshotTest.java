package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfPageArea;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewportArea;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageAreaScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;
    @Mock
    ImageCropper cropper;
    @Mock
    PlatformScreenshoter screenshoter;
    @Mock
    ImageScaler scaler;
    private final Rectangle rectangle = new Rectangle(2, 3, 4, 6);

    @Test
    @DisplayName("Should return screenshot of area")
    void shouldReturnScreenshot() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(screenshoter.takeWholePageScreenshot(webDriver, cropper, scaler)).thenReturn(image1);
        when(screenshoter.accept(any())).thenReturn(true);
        when(cropper.crop(image1, rectangle)).thenReturn(image2);
        BufferedImage screenshot = screenshotOfPageArea(2, 3, 6, 4)
                .take(webDriver, cropper, scaler, singletonList(screenshoter));
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        assertThat(screenshotOfPageArea(rectangle).describe())
                .isEqualTo("the area [x:2, y:3, width:6, height: 4]");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        assertThat(screenshotOfPageArea(rectangle).as("some element").describe())
                .isEqualTo("some element (the area [x:2, y:3, width:6, height: 4])");
    }

}
