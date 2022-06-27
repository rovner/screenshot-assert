package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.*;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.*;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.androidPhone;
import static java.time.Duration.ofSeconds;

@Feature("Framework: Junit5")
@Story("android phone")
public class AndroidPhoneExamplesTest extends BaseTest {
    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .scrollSleepTimeout(ofSeconds(1))
            .scrollMarginPixels(8)
            .viewportCropper(aggregating(capabilities(), floatingHeaderCutting()));


    @BeforeEach
    void beforeEach() throws IOException {
       wd = androidPhone();
    }

    @Test
    @DisplayName("Screenshot of the viewport")
    void testScreenshotOfViewport() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(animationSelector))
                .isEqualToReferenceId("viewport");
    }

    @Test
    @DisplayName("Screenshot of the whole page")
    void testFullPageScreenshot() {
        goToGithubMarketplacePage();
        screenshotAssert.assertThat(screenshotOfWholePage())
                .isEqualToReferenceId("page");
    }


    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        goToGithubMainPage();
        screenshotAssert.assertThat(screenshotOfElementFoundBy(textSelector))
                .isEqualToReferenceId("selector");
    }
}
