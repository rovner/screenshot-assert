package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.github.rovner.screenshot.assertions.junit.SoftScreenshotAssertExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;

@Feature("Framework: Junit5")
public class Junit5ExamplesTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd);

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
    @DisplayName("Screenshot of the viewport")
    void testFullPageScreenshot() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page");
    }

    @Test
    @DisplayName("Screenshot with diff (should be failed)")
    void testScreenshotWithDiff() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored area")
    void testScreenshotWithAreaIgnoring() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(area(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot with ignored web element")
    void testScreenshotWithElementIgnoring() {
        WebElement element = wd.findElement(textBy);
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(element(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot with ignored selector")
    void testScreenshotWithElementByIgnoring() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(textBy))
                .isEqualToReferenceId("by");
    }

    @Test
    @DisplayName("Screenshot with ignored diff hash code")
    void testScreenshotWithHashCodeIgnoring() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(hash(169794039))
                .isEqualToReferenceId("hash");
    }

    @Test
    @DisplayName("Screenshot of the area")
    void testAreaScreenshot() {
        screenshotAssert.assertThat(screenshotOfArea(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot of the web element")
    void testElementScreenshot() {
        WebElement element = wd.findElement(textBy);
        screenshotAssert.assertThat(screenshotOfElement(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        screenshotAssert.assertThat(screenshotOfElementFoundBy(textBy))
                .isEqualToReferenceId("by");
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 2})
    @DisplayName("Screenshot for parametrized tests")
    void testParametrized(int id) {
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page");
    }
}
