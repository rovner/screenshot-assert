package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.junit.SoftScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
@Feature("Framework: Junit5")
@Story("chrome")
public class SoftExamplesTest {
    private WebDriver wd;

    @RegisterExtension
    private final SoftScreenshotAssertExtension softScreenshotAssert = new SoftScreenshotAssertExtension(() -> wd);

    private final By textBy = By.cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        wd = new RemoteWebDriver(new URL("http://localhost:9515/"), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        wd.get("https://github.com/");
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @Test
    @DisplayName("Screenshots with soft assertions (should be failed)")
    void testScreenshotSoftAssertions() {
        softScreenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff_1");
        softScreenshotAssert.assertThat(screenshotOfElementFoundBy(textBy))
                .isEqualToReferenceId("diff_2");
    }
}
