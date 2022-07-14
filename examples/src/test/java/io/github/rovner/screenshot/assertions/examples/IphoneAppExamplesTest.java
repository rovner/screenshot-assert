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

import java.io.IOException;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfNativeElementFoundBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewport;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.iphoneTestApp;

@Feature("Framework: Junit5")
@Story("iphone")
public class IphoneAppExamplesTest extends BaseTest {

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .viewportCropper(aggregating(fixedHeaderCutting(140), fixedFooterCutting(60)));

    @BeforeEach
    void beforeEach() throws IOException {
        wd = iphoneTestApp();
    }

    @Test
    @DisplayName("Screenshot of the app")
    void testScreenshotOfViewport() {
        screenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("viewport");
    }

    @Test
    @DisplayName("Screenshot of the selector")
    void testElementByScreenshot() {
        By label = By.xpath("//XCUIElementTypeNavigationBar[@name=\"All Photos\"]");
        screenshotAssert.assertThat(screenshotOfNativeElementFoundBy(label))
                .isEqualToReferenceId("label");
    }
}
