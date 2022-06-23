package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssertBuilder;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssertConfig;
import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.reference.DefaultReferenceStorage;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

import static org.openqa.selenium.remote.CapabilityType.*;

/**
 * Screenshot assert junit 5 extension.
 */
public abstract class AbstractScreenshotAssertExtension implements BeforeEachCallback {

    private String testClass;
    private String testMethod;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> parametrizedTestId;
    private final Supplier<WebDriver> webDriverSupplier;
    private ImageDiffer imageDiffer = new DefaultImageDiffer();
    private AllureListener allureListener = new DefaultAllureListener();
    private ScreenshotAssertBuilder screenshotAssertBuilder;

    public AbstractScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        this.webDriverSupplier = webDriverSupplier;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        testClass = context.getRequiredTestClass().getCanonicalName();
        testMethod = context.getRequiredTestMethod().getName();
        parametrizedTestId = context.getUniqueId().contains("#")
                ? Optional.of(context.getUniqueId().replaceAll(".*#", "").replace("]", ""))
                : Optional.empty();
    }

    /**
     * Sets image differ to be used in screenshot comparison.
     *
     * @param imageDiffer differ to use.
     * @return self.
     */
    public AbstractScreenshotAssertExtension setImageDiffer(ImageDiffer imageDiffer) {
        this.imageDiffer = imageDiffer;
        return this;
    }

    /**
     * Sets allure listener to be used for reporting.
     *
     * @param allureListener listener to use.
     * @return self.
     */
    public AbstractScreenshotAssertExtension setAllureListener(AllureListener allureListener) {
        this.allureListener = allureListener;
        return this;
    }

    /**
     * Build and return screenshot assert.
     *
     * @param screenshot to take.
     * @return assert.
     */
    public abstract ScreenshotAssert assertThat(Screenshot screenshot);

    protected ScreenshotAssertBuilder getScreenshotAssertBuilder() {
        if (screenshotAssertBuilder == null) {
            WebDriver webDriver = webDriverSupplier.get();
            screenshotAssertBuilder = ScreenshotAssertBuilder.builder()
                    .setWebDriver(webDriver)
                    .setReferenceStorage(new DefaultReferenceStorage(resolveReferencesPath(webDriver)))
                    .setImageDiffer(imageDiffer)
                    .setAllureListener(allureListener);
        }
        return screenshotAssertBuilder;
    }

    private Path resolveReferencesPath(WebDriver webDriver) {
        ScreenshotAssertConfig cfg = ConfigFactory.create(ScreenshotAssertConfig.class);
        Path references = Paths.get(cfg.referencesBaseDir());
        if (webDriver instanceof HasCapabilities) {
            Capabilities capabilities = ((HasCapabilities) webDriver).getCapabilities();
            references = resolvePathForCapability(references, capabilities, PLATFORM_NAME);
            references = resolvePathForCapability(references, capabilities, BROWSER_NAME);
            references = resolvePathForCapability(references, capabilities, BROWSER_VERSION);
        }
        references = references.resolve(testClass.replaceAll("\\.", "/"))
                .resolve(testMethod);
        if (parametrizedTestId.isPresent()) {
            references = references.resolve(parametrizedTestId.get());
        }
        return references;
    }

    private Path resolvePathForCapability(Path references, Capabilities capabilities, String capabilityName) {
        try {
            String capability = (String) capabilities.getCapability(capabilityName);
            if (capability != null) {
                references = references.resolve(capability);
            }
        } catch (Exception ignored) {
        }
        return references;
    }
}
