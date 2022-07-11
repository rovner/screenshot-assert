package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.IOException;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.chrome;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

@Feature("Framework: Junit5")
@Story("chrome")
public class ChromeExamplesTest extends BaseTest {

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd);

    @BeforeEach
    void beforeEach() throws IOException {
        wd = chrome();
    }

    @Test
    @DisplayName("Screenshot of the viewport")
    void testViewportScreenshot() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("viewport");
    }

    @Test
    @DisplayName("Screenshot of the whole page")
    void testFullPageScreenshot() {
        goToGithubMarketplacePage();
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(area(790, 0, 10, 3030))
                .ignoring(elementsBy(xpath("//*[./h3[text()='Apps']]")))
                .isEqualToReferenceId("page");
    }

    @Test
    @DisplayName("Screenshot with diff (should be failed)")
    void testScreenshotWithDiff() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with ignored area")
    void testScreenshotWithAreaIgnoring() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(area(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot with ignored web element")
    void testScreenshotWithElementIgnoring() {
        goToGithubMainPage();
        WebElement element = wd.findElement(textSelector);
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(element(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot with ignored selector")
    void testScreenshotWithElementByIgnoring() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(textSelector))
                .isEqualToReferenceId("by");
    }

    @Test
    @DisplayName("Screenshot with ignored diff hash code")
    void testScreenshotWithHashCodeIgnoring() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(hash(169794039))
                .isEqualToReferenceId("hash");
    }

    @Test
    @DisplayName("Screenshot of the area")
    void testAreaScreenshot() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewportArea(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot of the area with different size (should be failed)")
    void testAreaScreenshotDifferentSize() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewportArea(20, 140, 700, 300))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot of the area ignoring element")
    void testAreaScreenshotWithIgnoring() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewportArea(20, 140, 640, 120))
                .ignoring(elementsBy(textSelector))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot of the web element")
    void testElementScreenshot() {
        goToGithubMainPage();
        WebElement element = wd.findElement(textSelector);
        screenshotAssert.assertThat(screenshotOfElement(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot of the web element outside of the viewport")
    void testElementScreenshotNotInViewport() {
        goToGithubMarketplacePage();
        ((JavascriptExecutor) wd).executeScript("window.scrollTo(0, 400)");
        By footer = cssSelector(".MarketplaceBody .container-lg + .container-lg");
        screenshotAssert.assertThat(screenshotOfElementFoundBy(footer))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfElementFoundBy(textSelector))
                .isEqualToReferenceId("by");
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 2})
    @DisplayName("Screenshot for parametrized tests")
    void testParametrized(int id) {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("full_page");
    }
}
