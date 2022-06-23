package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
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
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;

    @Mock
    private WebElement webElement;
    @Mock
    ImageCropper cropper;
    private final Rectangle rectangle = new Rectangle(2, 3, 4, 6);

    @Test
    @DisplayName("Should return screenshot of element")
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        ViewportScreenshotTest.mockDriverResponses(webDriver, image);
        when(webElement.getRect()).thenReturn(rectangle);
        BufferedImage screenshot = Screenshots.screenshotOfElement(webElement).take(webDriver, cropper);
        BufferedImage reference = new BufferedImage(6, 4, TYPE_4BYTE_ABGR);
        Optional<ImageDiff> diff = new DefaultImageDiffer().makeDiff(screenshot, reference, emptyList());
        assertThat(diff).isEmpty();
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
