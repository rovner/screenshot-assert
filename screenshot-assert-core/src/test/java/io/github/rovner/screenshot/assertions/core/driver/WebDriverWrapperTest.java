package io.github.rovner.screenshot.assertions.core.driver;

import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toByteArray;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.openqa.selenium.Platform.IOS;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@ExtendWith(MockitoExtension.class)
public class WebDriverWrapperTest {
    @Mock
    RemoteWebDriver webDriver;
    @Mock
    WebDriver pureWebDriver;
    @Mock
    Capabilities capabilities;
    WebDriverWrapper wrapper;
    DefaultImageDiffer differ = new DefaultImageDiffer();

    @BeforeEach
    void before() {
        wrapper = new WebDriverWrapper(webDriver);
    }

    @DisplayName("Should return web driver")
    @Test
    void shouldUnwrap() {
        assertThat(wrapper.getWebDriver()).isEqualTo(webDriver);
    }

    @DisplayName("Should take screenshot")
    @Test
    void shouldTakeScreenshot() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        when(webDriver.getScreenshotAs(any())).thenReturn(toByteArray(image));
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        BufferedImage screenshot = wrapper.takeScreenshot();
        assertThat(differ.makeDiff(screenshot, image, emptySet(), emptySet())).isEmpty();
    }

    @DisplayName("Should take screenshot without disabling caret blinking")
    @ParameterizedTest
    @ValueSource(strings = {"app", "bundleId", "appPackage", "appActivity"})
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldTakeScreenshotWithoutDisablingCaretBlinking(String capability) {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        when(webDriver.getScreenshotAs(any())).thenReturn(toByteArray(image));
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(capability)).thenReturn("test");
        BufferedImage screenshot = wrapper.takeScreenshot();
        assertThat(differ.makeDiff(screenshot, image, emptySet(), emptySet())).isEmpty();
        verify(webDriver, times(0)).executeScript(anyString());
    }

    @DisplayName("Should take screenshot even with disable caret blinking error")
    @Test
    void shouldTakeScreenshotWithError() {
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        when(webDriver.getScreenshotAs(any())).thenReturn(toByteArray(image));
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(webDriver.executeScript(anyString())).thenThrow(new RuntimeException("test"));
        BufferedImage screenshot = wrapper.takeScreenshot();
        assertThat(differ.makeDiff(screenshot, image, emptySet(), emptySet())).isEmpty();
    }

    @DisplayName("Should execute script")
    @Test
    void shouldExecuteScript() {
        when(webDriver.executeScript(anyString())).thenReturn(1L);
        Long value = wrapper.executeScript("");
        assertThat(value).isEqualTo(1L);
    }

    @DisplayName("Should throw exception if can not execute script")
    @Test
    void shouldThrowException() {
        assertThatThrownBy(() -> new WebDriverWrapper(pureWebDriver).executeScript(""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageMatching("Web Driver implementation org.openqa.selenium.WebDriver.* does not implement org.openqa.selenium.JavascriptExecutor");
    }

    @DisplayName("Should return browser name")
    @Test
    void shouldReturnBrowserName() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getBrowserName()).thenReturn("test");
        assertThat(wrapper.getBrowserName()).isEqualTo("test");
    }

    @DisplayName("Should return browser version")
    @Test
    void shouldReturnBrowserVersion() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getBrowserVersion()).thenReturn("10.0");
        assertThat(wrapper.getBrowserVersion()).isEqualTo("10.0");
    }

    @DisplayName("Should return device name")
    @Test
    void shouldReturnDeviceName() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability("deviceName")).thenReturn("ipad");
        assertThat(wrapper.getDeviceName()).isEqualTo("ipad");
    }

    @DisplayName("Should return platform")
    @Test
    void shouldReturnPlatform() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(PLATFORM_NAME)).thenReturn(IOS);
        assertThat(wrapper.getPlatform()).isEqualTo(IOS);
    }

    @DisplayName("Should return capability")
    @Test
    void shouldReturnCapability() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability("custom")).thenReturn("test");
        String custom = wrapper.getCapability("custom");
        assertThat(custom).isEqualTo("test");
    }

    @DisplayName("Should throw exception if can not get capabilities")
    @Test
    void shouldThrowException2() {
        assertThatThrownBy(() -> new WebDriverWrapper(pureWebDriver).getCapability(""))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageMatching("Web Driver implementation org.openqa.selenium.WebDriver.* does not implement org.openqa.selenium.HasCapabilities");
    }
}
