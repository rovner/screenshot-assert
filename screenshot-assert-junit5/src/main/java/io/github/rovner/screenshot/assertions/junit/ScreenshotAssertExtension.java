package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssertion;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

/**
 * Junit 5 screenshot assertion extension.
 */
public class ScreenshotAssertExtension extends BaseScreenshotAssertExtension<ScreenshotAssertExtension> {
    public ScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        super(webDriverSupplier);
    }

    @Override
    public ScreenshotAssertion assertThat(Screenshot screenshot) {
        return getScreenshotAssertions().assertThat(screenshot);
    }
}
