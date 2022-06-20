package com.github.rovner.screenshot.assertions.core.screenshot;

import com.github.rovner.screenshot.assertions.core.ImageUtils;
import com.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.OutputType.BYTES;

@ExtendWith(MockitoExtension.class)
public class WholePageScreenshotTest {

    @Mock
    private RemoteWebDriver webDriver;

    @Test
    void shouldReturnScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        mockDriverResponses(webDriver, image);
        BufferedImage screenshot = Screenshots.screenshotOfWholePage().take(webDriver);
        Optional<ImageDiff> diff = new DefaultImageDiffer().makeDiff(image, screenshot, emptyList());
        assertThat(diff).isEmpty();
    }

    @Test
    void shouldReturnRetinaScreenshot() {
        BufferedImage image = new BufferedImage(20, 20, TYPE_4BYTE_ABGR);
        mockDriverResponses(webDriver, image);
        BufferedImage screenshot = Screenshots.screenshotOfWholePage().take(webDriver);
        assertThat(screenshot.getWidth()).isEqualTo(10);
        assertThat(screenshot.getHeight()).isEqualTo(10);
    }

    @Test
    void shouldDescribe() {
        Assertions.assertThat(Screenshots.screenshotOfWholePage().describe()).isEqualTo("the whole page");
    }

    public static void mockDriverResponses(WebDriver webDriver, BufferedImage image) {
        when(((TakesScreenshot) webDriver).getScreenshotAs(BYTES)).thenReturn(ImageUtils.toByteArray(image));
        when(((JavascriptExecutor) webDriver).executeScript(anyString())).thenReturn(new HashMap<String, Long>() {{
            put("width", 10L);
            put("height", 10L);
        }});
    }
}
