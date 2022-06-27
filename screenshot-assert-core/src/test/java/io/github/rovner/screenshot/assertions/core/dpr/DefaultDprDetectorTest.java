package io.github.rovner.screenshot.assertions.core.dpr;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class DefaultDprDetectorTest {
    @Mock
    WebDriverWrapper webDriver;

    DprDetector dprDetector = new DefaultDprDetector();

    @Test
    @DisplayName("Should return dpr from capabilities (long)")
    void shouldReturnDprFromCapabilitiesLong() {
        when(webDriver.getCapability("pixelRatio")).thenReturn(2L);
        assertThat(dprDetector.detect(webDriver)).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should return dpr from capabilities (double)")
    void shouldReturnDprFromCapabilitiesDouble() {
        when(webDriver.getCapability("pixelRatio")).thenReturn(2.5);
        assertThat(dprDetector.detect(webDriver)).isEqualTo(2.5);
    }

    @Test
    @DisplayName("Should return dpr by calculating (not equal)")
    void shouldReturnDprByCalculating1() {
        when(webDriver.getCapability("pixelRatio")).thenReturn(null);
        when(webDriver.takeScreenshot()).thenReturn(new BufferedImage(10, 20, TYPE_INT_RGB));
        when(webDriver.executeScript(anyString())).thenReturn(5L);
        assertThat(dprDetector.detect(webDriver)).isEqualTo(2.0);
    }

    @Test
    @DisplayName("Should return dpr by calculating (equal)")
    void shouldReturnDprByCalculating2() {
        when(webDriver.getCapability("pixelRatio")).thenReturn(null);
        when(webDriver.takeScreenshot()).thenReturn(new BufferedImage(10, 20, TYPE_INT_RGB));
        when(webDriver.executeScript(anyString())).thenReturn(10L);
        assertThat(dprDetector.detect(webDriver)).isEqualTo(1.0);
    }
}
