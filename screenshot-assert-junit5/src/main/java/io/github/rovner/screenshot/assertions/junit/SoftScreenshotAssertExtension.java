package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssertion;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import io.github.rovner.screenshot.assertions.core.soft.DefaultSoftExceptionCollector;
import io.github.rovner.screenshot.assertions.core.soft.SoftExceptionCollector;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

/**
 * Junit 5 soft screenshot assertion extension.
 */
public class SoftScreenshotAssertExtension extends BaseScreenshotAssertExtension<SoftScreenshotAssertExtension> implements AfterEachCallback {

    public SoftScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        super(webDriverSupplier);
        configuration.setSoft(true);
    }

    /**
     * Sets soft exception collector to be used.
     * Default: {@link DefaultSoftExceptionCollector}
     *
     * @param softExceptionCollector collector to use.
     * @return self.
     */
    public SoftScreenshotAssertExtension softExceptionCollector(SoftExceptionCollector softExceptionCollector) {
        configuration.setSoftExceptionCollector(softExceptionCollector);
        return this;
    }

    @Override
    public ScreenshotAssertion assertThat(Screenshot screenshot) {
        return getScreenshotAssertions().assertThat(screenshot);
    }
    
    @Override
    public void afterEach(ExtensionContext context) {
        getScreenshotAssertions().assertAll();
    }
}
