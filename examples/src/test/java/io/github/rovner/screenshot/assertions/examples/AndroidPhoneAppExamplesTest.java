package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.capabilities;
import static io.github.rovner.screenshot.assertions.core.dpr.FixedDprDetector.fixed;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.area;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfNativeElementFoundBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewport;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.androidTestApp;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Feature("Framework: Junit5")
@Story("android phone")
public class AndroidPhoneAppExamplesTest extends BaseTest {
    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .dprDetector(fixed(1))
            .viewportCropper(capabilities());


    @BeforeEach
    void beforeEach() throws IOException {
        wd = androidTestApp("Pixel 5");
        new WebDriverWait(wd, ofSeconds(30))
                .until(presenceOfElementLocated(id("com.google.android.apps.photos:id/touch_outside")))
                .click();
    }

    @Test
    @DisplayName("Screenshot of the app")
    void testScreenshotOfViewport() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(area(145, 1005, 40, 350))
                .isEqualToReferenceId("viewport");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        By id = id("com.google.android.apps.photos:id/empty_page_image");
        screenshotAssert.assertThat(screenshotOfNativeElementFoundBy(id))
                .isEqualToReferenceId("selector");
    }
}
