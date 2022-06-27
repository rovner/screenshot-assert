package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElement;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementScreenshotTest {

    @Mock
    WebElement webElement;
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
    @DisplayName("Should return screenshot of element from viewport")
    void shouldReturnScreenshotOfViewportElement() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(webDriver.takeScreenshot()).thenReturn(image1);
        when(dprDetector.detect(webDriver)).thenReturn(1.0);
        when(viewportCropper.crop(image1, imageCropper, webDriver, 1.0)).thenReturn(image1);
        when(scaler.scale(image1, 1.0)).thenReturn(image1);
        when(imageCropper.crop(image1, rectangle)).thenReturn(image2);
        when(webDriver.executeScript(anyString(), eq(webElement))).thenReturn(new HashMap<String, Object>() {{
            put("visible", true);
            put("x", 2L);
            put("y", 3L);
            put("height", 4L);
            put("width", 6L);
        }});
        BufferedImage screenshot = screenshotOfElement(webElement).take(webDriver, configuration);
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should return screenshot of element from full pages")
    void shouldReturnScreenshotOfPageElement() {
        BufferedImage image1 = new BufferedImage(400, 200, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(dprDetector.detect(webDriver)).thenReturn(2.0);
        when(webDriver.executeScript(contains("return "))).thenReturn(new HashMap<String, Long>() {{
            put("viewportWidth", 200L);
            put("viewportHeight", 100L);
            put("documentWidth", 300L);
            put("documentHeight", 260L);
        }});
        when(webDriver.takeScreenshot()).thenReturn(image1);
        when(viewportCropper.crop(image1, imageCropper, webDriver, 2.0)).thenReturn(image1);
        when(imageCropper.crop(any(), any())).thenAnswer(invocation -> {
            Rectangle rect = invocation.getArgument(1);
            if (rect.getWidth() == 400) {
                return invocation.getArgument(0);
            } else {
                return image2;
            }
        });
        when(scaler.scale(any(), anyDouble())).thenAnswer(invocation -> {
            BufferedImage original = invocation.getArgument(0);
            return new BufferedImage(original.getWidth() / 2, original.getHeight() / 2, TYPE_4BYTE_ABGR);
        });
        when(webDriver.executeScript(anyString(), eq(webElement))).thenReturn(new HashMap<String, Object>() {{
            put("visible", false);
            put("x", 2L);
            put("y", 3L);
            put("height", 4L);
            put("width", 6L);
        }});
        BufferedImage screenshot = screenshotOfElement(webElement).take(webDriver, configuration);
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        when(webElement.toString()).thenReturn("web-element-to-string");
        Assertions.assertThat(Screenshots.screenshotOfElement(webElement).describe())
                .isEqualTo("the element : web-element-to-string");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        when(webElement.toString()).thenReturn("web-element-to-string");
        Assertions.assertThat(Screenshots.screenshotOfElement(webElement).as("some element").describe())
                .isEqualTo("some element (the element : web-element-to-string)");
    }

}
