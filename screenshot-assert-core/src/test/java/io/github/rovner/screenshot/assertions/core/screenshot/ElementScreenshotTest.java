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
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElement;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;
    @Mock
    private WebElement webElement;
    @Mock
    ImageCropper cropper;
    @Mock
    PlatformScreenshoter screenshoter;
    @Mock
    ImageScaler scaler;
    private final Rectangle rectangle = new Rectangle(2, 3, 4, 6);

    @Test
    @DisplayName("Should return screenshot of element from viewport")
    void shouldReturnScreenshotOfViewportElement() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(screenshoter.takeViewportScreenshot(webDriver, cropper, scaler)).thenReturn(image1);
        when(screenshoter.accept(any())).thenReturn(true);
        when(cropper.crop(image1, rectangle)).thenReturn(image2);
        when(webDriver.executeScript(anyString(), eq(webElement))).thenReturn(new HashMap<String, Object>() {{
            put("visible", true);
            put("x", 2L);
            put("y", 3L);
            put("height", 4L);
            put("width", 6L);
        }});
        BufferedImage screenshot = screenshotOfElement(webElement)
                .take(webDriver, cropper, scaler, singletonList(screenshoter));
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should return screenshot of element from full pages")
    void shouldReturnScreenshotOfPageElement() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(screenshoter.takeWholePageScreenshot(webDriver, cropper, scaler)).thenReturn(image1);
        when(screenshoter.accept(any())).thenReturn(true);
        when(cropper.crop(image1, rectangle)).thenReturn(image2);
        when(webDriver.executeScript(anyString(), eq(webElement))).thenReturn(new HashMap<String, Object>() {{
            put("visible", false);
            put("x", 2L);
            put("y", 3L);
            put("height", 4L);
            put("width", 6L);
        }});
        BufferedImage screenshot = screenshotOfElement(webElement)
                .take(webDriver, cropper, scaler, singletonList(screenshoter));
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
