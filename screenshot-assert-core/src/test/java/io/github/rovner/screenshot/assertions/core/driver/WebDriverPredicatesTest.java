package io.github.rovner.screenshot.assertions.core.driver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.RemoteWebDriver;

import static io.github.rovner.screenshot.assertions.core.driver.WebDriverPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.Platform.ANDROID;
import static org.openqa.selenium.Platform.IOS;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@ExtendWith(MockitoExtension.class)
public class WebDriverPredicatesTest {

    @Mock
    RemoteWebDriver webDriver;

    @Mock
    Capabilities capabilities;

    @Test
    @DisplayName("Should match any")
    void shouldMatchAny() {
        assertThat(any().test(webDriver)).isTrue();
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"windows", "xp", "mac", "linux", "unix"})
    @DisplayName("Should match desktop")
    void shouldMatchDesktop(String platform) {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(PLATFORM_NAME)).thenReturn(Platform.fromString(platform));
        assertThat(isDesktop().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should not match desktop")
    void shouldNotMatchDesktop() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(PLATFORM_NAME)).thenReturn(null);
        assertThat(isDesktop().test(webDriver)).isFalse();
    }

    @Test
    @DisplayName("Should match ios")
    void shouldMatchIos() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(PLATFORM_NAME)).thenReturn(IOS);
        assertThat(isIos().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should match android")
    void shouldMatchAndroid() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability(PLATFORM_NAME)).thenReturn(ANDROID);
        assertThat(isAndroid().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should match ipad")
    void shouldMatchIpad() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability("deviceName")).thenReturn("iPad (9th generation)");
        assertThat(isIpad().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should match iphone")
    void shouldMatchIphone() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getCapability("deviceName")).thenReturn("iPhone 13");
        assertThat(isIphone().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should match safari")
    void shouldMatchSafari() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getBrowserName()).thenReturn("Safari");
        assertThat(isSafari().test(webDriver)).isTrue();
    }

    @Test
    @DisplayName("Should match chrome")
    void shouldMatchChrome() {
        when(webDriver.getCapabilities()).thenReturn(capabilities);
        when(capabilities.getBrowserName()).thenReturn("Chrome");
        assertThat(isChrome().test(webDriver)).isTrue();
    }
}
