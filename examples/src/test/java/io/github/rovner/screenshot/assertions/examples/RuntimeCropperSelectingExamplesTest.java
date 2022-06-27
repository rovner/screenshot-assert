package io.github.rovner.screenshot.assertions.examples;


import io.github.rovner.screenshot.assertions.examples.utils.BaseTest;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.*;
import static io.github.rovner.screenshot.assertions.core.driver.WebDriverPredicates.*;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewport;
import static io.github.rovner.screenshot.assertions.examples.utils.Drivers.ios;
import static java.time.Duration.ofSeconds;

@Feature("Framework: Junit5")
@Story("runtime detecting browser")
public class RuntimeCropperSelectingExamplesTest extends BaseTest {

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
            .scrollSleepTimeout(ofSeconds(500))
            .viewportCropper(matching()
                            .match(isDesktop(), desktop())
                            .match(isIos().and(isIpad()).and(isSafari()), floatingHeaderCutting())
                            .match(isIos().and(isIphone()).and(isSafari()), aggregating(
                                    fixedHeaderCutting(140),
                                    floatingFooterCutting()))
                            .match(isAndroid(), aggregating(capabilities(), floatingHeaderCutting()))
            );


    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {"iPhone 13", "iPad (9th generation)"})
    @DisplayName("Screenshot of the viewport on ")
    void testScreenshotOfViewport(String device) throws MalformedURLException {
        wd = ios(device);
        screenshotAssert.assertThat(screenshotOfViewport())
                .ignoring(elementsBy(animationSelector))
                .isEqualToReferenceId("viewport");
    }

}
