package io.github.rovner.screenshot.assertions.examples;


import io.appium.java_client.remote.MobileCapabilityType;
import io.github.rovner.screenshot.assertions.core.platform.CuttingBrowserScreenshooter;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;

import static io.github.rovner.screenshot.assertions.core.cutter.Cutters.fixedFooter;
import static io.github.rovner.screenshot.assertions.core.cutter.Cutters.floatingHeader;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.platform.Platforms.isAndroidChrome;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Arrays.asList;
import static org.openqa.selenium.By.cssSelector;

@Feature("Framework: Junit5")
@Story("android phone")
public class AndroidPhoneExamplesTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .addPlatformScreenshooter("android-phone",
                    new CuttingBrowserScreenshooter(ofMillis(1000), asList(fixedFooter(15), floatingHeader())) {
                        @Override
                        public boolean accept(Capabilities capabilities) {
                            return isAndroidChrome(capabilities);
                        }
                    });
    private final By textBy = cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.0");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 5");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        capabilities.setCapability("nativeWebScreenshot", true);

        wd = new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @Test
    @DisplayName("Screenshot of the viewport")
    void testScreenshotOfViewport() {
        wd.get("https://github.com/");
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(cssSelector(".home-globe-container")))
                .isEqualToReferenceId("viewport");
    }

    @Test
    @DisplayName("Screenshot of the whole page")
    void testFullPageScreenshot() {
        wd.get("https://github.com/marketplace?category=api-management&type=apps&verification=verified_creator");
        By loading = By.cssSelector("[src*='/marketplace/search']");
        new WebDriverWait(wd, ofSeconds(60))
                .until(ExpectedConditions.invisibilityOfElementLocated(loading));
        screenshotAssert.assertThat(screenshotOfWholePage())
                .isEqualToReferenceId("page");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        wd.get("https://github.com/");
        screenshotAssert.assertThat(screenshotOfElementFoundBy(textBy))
                .isEqualToReferenceId("by");
    }
}
