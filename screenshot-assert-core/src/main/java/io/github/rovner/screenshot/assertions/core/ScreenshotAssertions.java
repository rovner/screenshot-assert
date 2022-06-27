package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.exceptions.SoftAssertionError;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.openqa.selenium.WebDriver;

import java.util.Collection;

import static io.github.rovner.screenshot.assertions.core.ScreenshotAssertionProperties.properties;
import static java.util.Objects.requireNonNull;

/**
 * Screenshot assertions.
 */
public class ScreenshotAssertions {

    private final WebDriver webDriver;
    private final ScreenshotAssertionConfiguration configuration;
    private final ScreenshotAssertionProperties properties;

    public ScreenshotAssertions(WebDriver webDriver,
                                ScreenshotAssertionConfiguration configuration,
                                ScreenshotAssertionProperties properties) {
        this.webDriver = webDriver;
        this.configuration = configuration;
        this.properties = properties;
    }

    public ScreenshotAssertions(WebDriver webDriver, ScreenshotAssertionConfiguration configuration) {
        this(webDriver, configuration, properties());
    }

    /**
     * Creates new screenshot assertion.
     *
     * @param screenshot screenshot to take and compare.
     * @return new assertion.
     */
    public ScreenshotAssertion assertThat(Screenshot screenshot) {
        requireNonNull(webDriver, "Web driver can not be null");
        requireNonNull(configuration.getReferenceStorage(), "References storage can not be null");
        return new ScreenshotAssertion(webDriver, configuration, properties, screenshot);
    }

    /**
     * Collects all exceptions from soft assertions and throws {@link SoftAssertionError} if there are any.
     */
    public void assertAll() {
        if (!configuration.isSoft() && !properties.isSoft()) {
            throw new IllegalStateException(String.format("%s.assertAll supposed to be called only when isSoft=true",
                    this.getClass().getCanonicalName()));
        }
        Collection<Throwable> exceptions = configuration.getSoftExceptionCollector().getAll();
        if (!exceptions.isEmpty()) {
            SoftAssertionError error = new SoftAssertionError(String.format("%d assertion(s) failed", exceptions.size()));
            exceptions.forEach(error::addSuppressed);
            throw error;
        }
    }
}
