package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.exceptions.TooManyElementsException;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElementFoundBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfNativeElementFoundBy;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NativeElementFoundByScreenshotTest {

    @Mock
    WebDriverWrapper webDriverWrapper;
    @Mock
    WebDriver webDriver;
    @Mock
    WebElement webElement;
    @Mock
    ImageScaler scaler;
    @Mock
    ImageCropper imageCropper;
    @Mock
    DprDetector dprDetector;
    ScreenshotConfiguration configuration;
    Rectangle rectangle = new Rectangle(2, 3, 4, 6);
    By by = By.cssSelector(".test");

    @BeforeEach
    void beforeEach() {
        configuration = ScreenshotConfiguration
                .builder()
                .dprDetector(dprDetector)
                .imageCropper(imageCropper)
                .imageScaler(scaler)
                .build();
    }

    @Test
    @DisplayName("Should return screenshot of element by selector")
    void shouldReturnScreenshotOfViewportElement() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(webDriverWrapper.takeScreenshot()).thenReturn(image1);
        when(dprDetector.detect(webDriverWrapper)).thenReturn(1.0);
        when(scaler.scale(image1, 1.0)).thenReturn(image1);
        when(imageCropper.crop(image1, rectangle)).thenReturn(image2);
        when(webDriverWrapper.getWebDriver()).thenReturn(webDriver);
        when(webDriver.findElements(by)).thenReturn(singletonList(webElement));
        when(webElement.getRect()).thenReturn(rectangle);
        BufferedImage screenshot = screenshotOfNativeElementFoundBy(by).take(webDriverWrapper, configuration);
        assertThat(screenshot).isEqualTo(image2);
    }

    @Test
    @DisplayName("Should throw exception if there are more than one element found by selector")
    void shouldThrowException1() {
        when(webDriverWrapper.getWebDriver()).thenReturn(webDriver);
        when(webDriver.findElements(by)).thenReturn(asList(webElement, webElement));
        assertThatThrownBy(() -> screenshotOfNativeElementFoundBy(by).take(webDriverWrapper, configuration))
                .isInstanceOf(TooManyElementsException.class)
                .hasMessageContaining("More than one element found by By.cssSelector: .test");
    }

    @Test
    @DisplayName("Should throw exception if there are no element found by selector")
    void shouldThrowException2() {
        when(webDriverWrapper.getWebDriver()).thenReturn(webDriver);
        when(webDriver.findElements(by)).thenReturn(emptyList());
        assertThatThrownBy(() -> screenshotOfNativeElementFoundBy(by).take(webDriverWrapper, configuration))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No element found by By.cssSelector: .test");
    }

    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        assertThat(screenshotOfNativeElementFoundBy(by).describe())
                .isEqualTo("the element found by: By.cssSelector: .test");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        assertThat(screenshotOfNativeElementFoundBy(by).as("some element").describe())
                .isEqualTo("some element (the element found by: By.cssSelector: .test)");
    }

}
