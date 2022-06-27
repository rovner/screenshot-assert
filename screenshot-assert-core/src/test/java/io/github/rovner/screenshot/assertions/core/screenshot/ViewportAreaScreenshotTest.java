package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewportArea;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ViewportAreaScreenshotTest {

    @Mock
    WebDriverWrapper webDriver;
    @Mock
    ImageScaler scaler;
    @Mock
    ViewportCropper viewportCropper;
    @Mock
    ImageCropper imageCropper;
    @Mock
    DprDetector dprDetector;
    ScreenshotConfiguration configuration;
    Rectangle rectangle = new Rectangle(2, 3, 4, 6);

    @BeforeEach
    void beforeEach() {
        configuration = ScreenshotConfiguration
                .builder()
                .dprDetector(dprDetector)
                .imageCropper(imageCropper)
                .viewportCropper(viewportCropper)
                .imageScaler(scaler)
                .build();
    }

    @Test
    @DisplayName("Should return screenshot of viewport area")
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        when(webDriver.takeScreenshot()).thenReturn(image);
        when(dprDetector.detect(webDriver)).thenReturn(1.0);
        when(viewportCropper.crop(image, imageCropper, webDriver, 1.0)).thenReturn(image);
        when(scaler.scale(image, 1.0)).thenReturn(image);
        when(imageCropper.crop(image, rectangle)).thenReturn(image);

        BufferedImage screenshot = screenshotOfViewportArea(2, 3, 6, 4)
                .take(webDriver, configuration);

        assertThat(screenshot).isEqualTo(image);
    }

    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        assertThat(screenshotOfViewportArea(rectangle).describe())
                .isEqualTo("the area [x:2, y:3, width:6, height: 4]");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        assertThat(screenshotOfViewportArea(rectangle).as("some element").describe())
                .isEqualTo("some element (the area [x:2, y:3, width:6, height: 4])");
    }

}
