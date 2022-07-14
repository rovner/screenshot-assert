package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
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
import java.util.HashSet;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NativeElementScreenshotTest {

    @Mock
    WebElement webElement;
    @Mock
    WebDriverWrapper webDriver;
    @Mock
    ImageScaler scaler;
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
                .imageScaler(scaler)
                .build();
    }

    @Test
    @DisplayName("Should return screenshot of element from viewport")
    void shouldReturnScreenshotOfElement() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(webDriver.takeScreenshot()).thenReturn(image1);
        when(dprDetector.detect(webDriver)).thenReturn(1.0);
        when(scaler.scale(image1, 1.0)).thenReturn(image1);
        when(imageCropper.crop(image1, rectangle)).thenReturn(image2);
        when(webElement.getRect()).thenReturn(rectangle);
        BufferedImage screenshot = screenshotOfNativeElement(webElement).take(webDriver, configuration);
        assertThat(screenshot).isEqualTo(image2);
    }


    @Test
    @DisplayName("Should return default describe")
    void shouldDescribe1() {
        when(webElement.toString()).thenReturn("web-element-to-string");
        Assertions.assertThat(screenshotOfNativeElement(webElement).describe())
                .isEqualTo("the element : web-element-to-string");
    }

    @Test
    @DisplayName("Should return custom describe")
    void shouldDescribe2() {
        when(webElement.toString()).thenReturn("web-element-to-string");
        Assertions.assertThat(screenshotOfNativeElement(webElement).as("some element").describe())
                .isEqualTo("some element (the element : web-element-to-string)");
    }

    @Test
    @DisplayName("Should shift coordinates")
    void shouldShiftCoordinates() {
        BufferedImage image1 = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        BufferedImage image2 = new BufferedImage(5, 5, TYPE_4BYTE_ABGR);
        when(webDriver.takeScreenshot()).thenReturn(image1);
        when(dprDetector.detect(webDriver)).thenReturn(1.0);
        when(scaler.scale(image1, 1.0)).thenReturn(image1);
        when(imageCropper.crop(image1, rectangle)).thenReturn(image2);
        when(webElement.getRect()).thenReturn(rectangle);
        NativeElementScreenshot nativeElementScreenshot = screenshotOfNativeElement(webElement);
        nativeElementScreenshot.take(webDriver, configuration);

        HashSet<Rectangle> toShift = newHashSet(singletonList(new Rectangle(3, 5, 2, 3)));
        HashSet<Rectangle> shifted = newHashSet(singletonList(new Rectangle(1, 2, 2, 3)));
        assertThat(nativeElementScreenshot.shiftAreas(toShift))
                .isEqualTo(shifted);
    }
}
