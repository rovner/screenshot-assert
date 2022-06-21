package io.github.rovner.screenshot.assertions.examples.junit5;

import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;

import static io.appium.java_client.remote.MobileCapabilityType.*;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.area;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfWholePage;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

@Epic("Screenshot asserts")
@Feature("Junit5")
@Story("iOS")
public class IosExampleTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd);

    @BeforeEach
    void beforeEach() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(PLATFORM_NAME, "iOS");
        capabilities.setCapability(PLATFORM_VERSION, "15.5");
        capabilities.setCapability(AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(BROWSER_NAME, "Safari");
        capabilities.setCapability(DEVICE_NAME, "iPhone 13");

        wd = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        wd.get("https://github.com/");
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @Test
    @DisplayName("Full page screenshot")
    void testFullPageScreenshot() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(elementsBy(By.cssSelector(".home-globe-container")))
                .ignoring(area(0, 0, 390, 40))
                .isEqualToReferenceId("full_page");
    }
}
