package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.core.ScreenshotAssertionConfiguration;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssertions;
import io.github.rovner.screenshot.assertions.core.reference.DefaultReferenceStorage;
import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.examples.utils.Drivers;
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
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.chrome;

@Feature("No framework")
@Story("chrome")
public class NoFrameworkExamplesTest extends BaseTest {
    public static final Path REFERENCES = Paths.get("src/test/resources/references/")
            .resolve(NoFrameworkExamplesTest.class.getCanonicalName());

    private ScreenshotAssertions screenshotAssertions;
    private ScreenshotAssertions softScreenshotAssertions;

    @BeforeEach
    void beforeEach() throws IOException {
        wd = chrome();

        DefaultReferenceStorage storage = new DefaultReferenceStorage(REFERENCES);
        screenshotAssertions = new ScreenshotAssertions(wd, ScreenshotAssertionConfiguration.builder()
                .referenceStorage(storage)
                .build());

        softScreenshotAssertions = new ScreenshotAssertions(wd, ScreenshotAssertionConfiguration.builder()
                .referenceStorage(storage)
                .isSoft(true)
                .build());
    }

    @Test
    @DisplayName("Screenshot of the viewport")
    void testFullPageScreenshot() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page");
    }

    @Test
    @DisplayName("Screenshot with diff (should be failed)")
    void testScreenshotWithDiff() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored area")
    void testScreenshotWithAreaIgnoring() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .ignoring(area(20, 140, 640, 120))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored web element")
    void testScreenshotWithElementIgnoring() {
        goToGithubMainPage();
        WebElement element = wd.findElement(textSelector);
        screenshotAssertions.assertThat(screenshotOfViewport())
                .ignoring(element(element))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored selector")
    void testScreenshotWithElementByIgnoring() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(textSelector))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored diff hash code")
    void testScreenshotWithHashCodeIgnoring() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .ignoring(hash(169794039))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot of the area")
    void testAreaScreenshot() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewportArea(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot of the web element")
    void testElementScreenshot() {
        goToGithubMainPage();
        WebElement element = wd.findElement(textSelector);
        screenshotAssertions.assertThat(screenshotOfElement(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfElementFoundBy(textSelector))
                .isEqualToReferenceId("element");
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 2})
    @DisplayName("Screenshot for parametrized tests")
    void testParametrized(int id) {
        goToGithubMainPage();
        screenshotAssertions.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page_" + id);
    }

    @Test
    @DisplayName("Screenshots with soft assertions (should be failed)")
    void testScreenshotSoftAssertions() {
        goToGithubMainPage();
        softScreenshotAssertions.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff");
        softScreenshotAssertions.assertThat(screenshotOfElementFoundBy(textSelector))
                .isEqualToReferenceId("element_diff");
        softScreenshotAssertions.assertAll();
    }
}
