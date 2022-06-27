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
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfWholePage;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageScreenshotTest {

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

    @BeforeEach
    void beforeEach() {
        configuration = ScreenshotConfiguration
                .builder()
                .dprDetector(dprDetector)
                .imageCropper(imageCropper)
                .viewportCropper(viewportCropper)
                .imageScaler(scaler)
                .scrollMarginPixels(0)
                .build();
    }

    @Test
    @DisplayName("Should return screenshot of whole page")
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(400, 200, TYPE_4BYTE_ABGR);
        when(dprDetector.detect(webDriver)).thenReturn(2.0);
        when(webDriver.executeScript(contains("return "))).thenReturn(new HashMap<String, Long>() {{
            put("viewportWidth", 200L);
            put("viewportHeight", 100L);
            put("documentWidth", 300L);
            put("documentHeight", 260L);
        }});
        when(webDriver.takeScreenshot()).thenReturn(image);
        when(viewportCropper.crop(image, imageCropper, webDriver, 2.0)).thenReturn(image);
        when(imageCropper.crop(image, new Rectangle(0, 0, 200, 400))).thenReturn(image);
        when(scaler.scale(any(), anyDouble())).thenAnswer(invocation -> {
            BufferedImage original = invocation.getArgument(0);
            return new BufferedImage(original.getWidth() / 2, original.getHeight() / 2, TYPE_4BYTE_ABGR);
        });

        BufferedImage screenshot = screenshotOfWholePage().take(webDriver, configuration);
        assertThat(screenshot.getHeight()).isEqualTo(260);
        assertThat(screenshot.getWidth()).isEqualTo(300);
    }

    @Test
    @DisplayName("Should throw exception if scroll margin is invalid")
    void shouldThrowExceptionOnInvalidScrollMargin() {
        configuration.setScrollMarginPixels(2);
        when(dprDetector.detect(webDriver)).thenReturn(2.75);
        assertThatThrownBy(() -> screenshotOfWholePage().take(webDriver, configuration))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ScrollMarginPixels * dpr should be an integer. Provided values dpr: 2.75, scrollMarginPixels: 2");
    }

    @Test
    @DisplayName("Should return screenshot of whole page without scrolling")
    void shouldReturnScreenshotWithoutScrolling() {
        BufferedImage image = new BufferedImage(200, 100, TYPE_4BYTE_ABGR);
        when(dprDetector.detect(webDriver)).thenReturn(1.0);
        when(webDriver.executeScript(contains("return "))).thenReturn(new HashMap<String, Long>() {{
            put("viewportWidth", 200L);
            put("viewportHeight", 100L);
            put("documentWidth", 200L);
            put("documentHeight", 100L);
        }});
        when(webDriver.takeScreenshot()).thenReturn(image);
        when(viewportCropper.crop(image, imageCropper, webDriver, 1.0)).thenReturn(image);
        when(imageCropper.crop(image, new Rectangle(0, 0, 100, 200))).thenReturn(image);
        when(scaler.scale(any(), anyDouble())).thenAnswer(invocation -> invocation.getArgument(0));

        BufferedImage screenshot = screenshotOfWholePage().take(webDriver, configuration);
        assertThat(screenshot.getHeight()).isEqualTo(100);
        assertThat(screenshot.getWidth()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should return describe")
    void shouldDescribe() {
        assertThat(screenshotOfWholePage().describe()).isEqualTo("the whole page");
    }
}
