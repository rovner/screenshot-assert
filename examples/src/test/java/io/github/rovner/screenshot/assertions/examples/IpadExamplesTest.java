package io.github.rovner.screenshot.assertions.examples;


import io.appium.java_client.remote.MobileCapabilityType;
import io.github.rovner.screenshot.assertions.core.cutter.Cutters;
import io.github.rovner.screenshot.assertions.core.cutter.FloatingHeaderCutter;
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

import static io.github.rovner.screenshot.assertions.core.cutter.Cutters.floatingHeader;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.area;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.platform.Platforms.isIpadSafari;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.cssSelector;

@Feature("Framework: Junit5")
@Story("ipad")
public class IpadExamplesTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .addPlatformScreenshooter("ipad-safari",
                    new CuttingBrowserScreenshooter(ofMillis(100), floatingHeader()) {
                        @Override
                        public boolean accept(Capabilities capabilities) {
                            return isIpadSafari(capabilities);
                        }
                    });

    private final By textBy = cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.5");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPad (9th generation)");

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

    @Test
    @DisplayName("Screenshot with ignored selector")
    void testScreenshotWithElementByIgnoring() {
        wd.get("https://github.com/");
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(textBy))
                .ignoring(elementsBy(cssSelector(".home-globe-container")))
                .isEqualToReferenceId("by");
    }
}
