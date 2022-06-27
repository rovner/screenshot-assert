package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.junit.SoftScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfElementFoundBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewport;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.chrome;

@Feature("Framework: Junit5")
@Story("chrome")
public class SoftExamplesTest extends BaseTest {
    @RegisterExtension
    private final SoftScreenshotAssertExtension softScreenshotAssert = new SoftScreenshotAssertExtension(() -> wd);

    @BeforeEach
    void beforeEach() throws IOException {
        wd = chrome();
    }

    @Test
    @DisplayName("Screenshots with soft assertions (should be failed)")
    void testScreenshotSoftAssertions() {
        goToGithubMainPage();
        softScreenshotAssert.assertThat(screenshotOfViewport())
                .isEqualToReferenceId("diff_1");
        softScreenshotAssert.assertThat(screenshotOfElementFoundBy(textSelector))
                .isEqualToReferenceId("diff_2");
    }
}
