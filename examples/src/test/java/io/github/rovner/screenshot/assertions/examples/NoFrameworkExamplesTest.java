package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.core.ScreenshotAssertBuilder;
import io.github.rovner.screenshot.assertions.core.reference.DefaultReferenceStorage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElementFoundBy;

@Feature("No framework")
@Story("chrome")
public class NoFrameworkExamplesTest {
    public static final Path REFERENCES = Paths.get("src/test/resources/references/")
            .resolve(NoFrameworkExamplesTest.class.getCanonicalName());
    private WebDriver wd;

    private ScreenshotAssertBuilder screenshotAssert;
    private ScreenshotAssertBuilder softScreenshotAssert;
    private final By textBy = By.cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        wd = new RemoteWebDriver(new URL("http://localhost:9515/"), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        wd.get("https://github.com/");

        screenshotAssert = ScreenshotAssertBuilder.builder()
                .setWebDriver(wd)
                .setReferenceStorage(new DefaultReferenceStorage(REFERENCES));

        softScreenshotAssert = ScreenshotAssertBuilder.builder()
                .setWebDriver(wd)
                .setReferenceStorage(new DefaultReferenceStorage(REFERENCES))
                .setSoft(true);
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
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored web element")
    void testScreenshotWithElementIgnoring() {
        WebElement element = wd.findElement(textBy);
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(element(element))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored selector")
    void testScreenshotWithElementByIgnoring() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(textBy))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored diff hash code")
    void testScreenshotWithHashCodeIgnoring() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(hash(169794039))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot of the area")
    void testAreaScreenshot() {
        screenshotAssert.assertThat(screenshotOfViewportArea(20, 140, 640, 120))
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
                .isEqualToReferenceId("element");
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 2})
    @DisplayName("Screenshot for parametrized tests")
    void testParametrized(int id) {
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page_" + id);
    }

    @Test
    @DisplayName("Screenshots with soft assertions (should be failed)")
    void testScreenshotSoftAssertions() {
        softScreenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff");
        softScreenshotAssert.assertThat(screenshotOfElementFoundBy(textBy))
                .isEqualToReferenceId("element_diff");
        softScreenshotAssert.assertAll();
    }
}
