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
import java.util.HashSet;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfPageArea;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewportArea;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PageAreaScreenshotTest {

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
                .scrollMarginPixels(0)
                .build();
    }

    @Test
    @DisplayName("Should return screenshot of area")
    void shouldReturnScreenshot() {
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

        BufferedImage screenshot = screenshotOfPageArea(2, 3, 6, 4).take(webDriver, configuration);
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

    @Test
    @DisplayName("Should shift coordinates")
    void shouldShiftCoordinates() {
        HashSet<Rectangle> toShift = newHashSet(singletonList(new Rectangle(3, 5, 2, 3)));
        HashSet<Rectangle> shifted = newHashSet(singletonList(new Rectangle(1, 2, 2, 3)));
        assertThat(screenshotOfPageArea(rectangle).shiftAreas(toShift))
                .isEqualTo(shifted);
    }

}
