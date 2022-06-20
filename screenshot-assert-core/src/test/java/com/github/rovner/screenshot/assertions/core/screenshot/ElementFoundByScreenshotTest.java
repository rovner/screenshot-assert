package com.github.rovner.screenshot.assertions.core.screenshot;

import com.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import com.github.rovner.screenshot.assertions.core.exceptions.TooManyElementsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementFoundByScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;

    @Mock
    private WebElement webElement;

    private final Rectangle rectangle = new Rectangle(2, 3, 4, 6);
    private final By by = By.cssSelector(".test");

    @Test
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        WholePageScreenshotTest.mockDriverResponses(webDriver, image);
        when(webDriver.findElements(by)).thenReturn(singletonList(webElement));
        when(webElement.getRect()).thenReturn(rectangle);
        BufferedImage screenshot = Screenshots.screenshotOfElementFoundBy(by).take(webDriver);
        BufferedImage reference = new BufferedImage(6, 4, TYPE_4BYTE_ABGR);
        Optional<ImageDiff> diff = new DefaultImageDiffer().makeDiff(screenshot, reference, emptyList());
        assertThat(diff).isEmpty();
    }

    @Test
    void shouldThrowException1() {
        when(webDriver.findElements(by)).thenReturn(asList(webElement, webElement));
        assertThatThrownBy(() -> Screenshots.screenshotOfElementFoundBy(by).take(webDriver))
                .isInstanceOf(TooManyElementsException.class)
                .hasMessageContaining("More than one element found by By.cssSelector: .test");
    }

    @Test
    void shouldThrowException2() {
        when(webDriver.findElements(by)).thenReturn(emptyList());
        assertThatThrownBy(() -> Screenshots.screenshotOfElementFoundBy(by).take(webDriver))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No element found by By.cssSelector: .test");
    }


    @Test
    void shouldDescribe1() {
        Assertions.assertThat(Screenshots.screenshotOfElementFoundBy(by).describe())
                .isEqualTo("the element found by: By.cssSelector: .test");
    }

    @Test
    void shouldDescribe2() {
        Assertions.assertThat(Screenshots.screenshotOfElementFoundBy(by).as("some element").describe())
                .isEqualTo("some element (the element found by: By.cssSelector: .test)");
    }

}
