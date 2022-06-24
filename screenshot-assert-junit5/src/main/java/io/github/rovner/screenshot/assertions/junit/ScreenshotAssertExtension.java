package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class ScreenshotAssertExtension extends AbstractScreenshotAssertExtension<ScreenshotAssertExtension> {
    public ScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        super(webDriverSupplier);
    }

    @Override
    public ScreenshotAssert assertThat(Screenshot screenshot) {
        return getScreenshotAssertBuilder().assertThat(screenshot);
    }
}
