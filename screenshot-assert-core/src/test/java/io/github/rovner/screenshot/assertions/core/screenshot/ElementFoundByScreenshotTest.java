package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.exceptions.TooManyElementsException;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElementFoundBy;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementFoundByScreenshotTest {

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
    private final By by = By.cssSelector(".test");

    @Test
    @DisplayName("Should return screenshot of element")
    void shouldReturnScreenshot() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(webDriver.findElements(by)).thenReturn(singletonList(webElement));
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
        BufferedImage screenshot = screenshotOfElementFoundBy(by)
                .take(webDriver, cropper, scaler, singletonList(screenshoter));
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should throw exception if there are more than one element found by selector")
    void shouldThrowException1() {
        when(webDriver.findElements(by)).thenReturn(asList(webElement, webElement));
        assertThatThrownBy(() -> screenshotOfElementFoundBy(by)
                .take(webDriver, cropper, scaler, singletonList(screenshoter)))
                .isInstanceOf(TooManyElementsException.class)
                .hasMessageContaining("More than one element found by By.cssSelector: .test");
    }

    @Test
    @DisplayName("Should throw exception if there are no element found by selector")
    void shouldThrowException2() {
        when(webDriver.findElements(by)).thenReturn(emptyList());
        assertThatThrownBy(() -> screenshotOfElementFoundBy(by)
                .take(webDriver, cropper, scaler, singletonList(screenshoter)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No element found by By.cssSelector: .test");
    }


    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        Assertions.assertThat(screenshotOfElementFoundBy(by).describe())
                .isEqualTo("the element found by: By.cssSelector: .test");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        Assertions.assertThat(screenshotOfElementFoundBy(by).as("some element").describe())
                .isEqualTo("some element (the element found by: By.cssSelector: .test)");
    }

}
