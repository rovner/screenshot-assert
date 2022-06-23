package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class SoftScreenshotAssertExtension extends AbstractScreenshotAssertExtension implements AfterEachCallback {

    public SoftScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        super(webDriverSupplier);
    }

    @Override
    public ScreenshotAssert assertThat(Screenshot screenshot) {
        return getScreenshotAssertBuilder()
                .setSoft(true)
                .assertThat(screenshot);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        getScreenshotAssertBuilder().assertAll();
    }
}
