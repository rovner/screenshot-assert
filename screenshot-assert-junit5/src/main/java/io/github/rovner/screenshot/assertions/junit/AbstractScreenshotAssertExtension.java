package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssertBuilder;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssertConfig;
import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.reference.DefaultReferenceStorage;
import io.github.rovner.screenshot.assertions.core.scaler.DefaultImageScaler;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static io.github.rovner.screenshot.assertions.core.ScreenshotAssertBuilder.getDefaultPlatformScreenshooters;
import static org.openqa.selenium.remote.CapabilityType.*;

/**
 * Screenshot assert junit 5 extension.
 */
public abstract class AbstractScreenshotAssertExtension<T extends AbstractScreenshotAssertExtension<T>> implements BeforeEachCallback {

    private String testClass;
    private String testMethod;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> parametrizedTestId;
    private final Supplier<WebDriver> webDriverSupplier;
    private ImageDiffer imageDiffer = new DefaultImageDiffer();
    private AllureListener allureListener = new DefaultAllureListener();
    private ImageScaler imageScaler = new DefaultImageScaler();
    private Map<String, PlatformScreenshoter> platformScreenshoters = getDefaultPlatformScreenshooters();
    private ScreenshotAssertBuilder screenshotAssertBuilder;

    protected AbstractScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
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
    public T setImageDiffer(ImageDiffer imageDiffer) {
        this.imageDiffer = imageDiffer;
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets allure listener to be used for reporting.
     *
     * @param allureListener listener to use.
     * @return self.
     */
    public T setAllureListener(AllureListener allureListener) {
        this.allureListener = allureListener;
        //noinspection unchecked
        return (T) this;
    }

    public T setImageScaler(ImageScaler imageScaler) {
        this.imageScaler = imageScaler;
        //noinspection unchecked
        return (T) this;
    }

    public T setPlatformScreenshoters(Map<String, PlatformScreenshoter> platformScreenshoters) {
        this.platformScreenshoters = platformScreenshoters;
        //noinspection unchecked
        return (T) this;
    }

    public T addPlatformScreenshooter(String id, PlatformScreenshoter platformScreenshoter) {
        this.platformScreenshoters.put("id", platformScreenshoter);
        //noinspection unchecked
        return (T) this;
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
                    .setImageScaler(imageScaler)
                    .setAllureListener(allureListener)
                    .setPlatformScreenshoters(platformScreenshoters);
        }
        return screenshotAssertBuilder;
    }

    private Path resolveReferencesPath(WebDriver webDriver) {
        ScreenshotAssertConfig cfg = ConfigFactory.create(ScreenshotAssertConfig.class);
        Capabilities capabilities = ((HasCapabilities) webDriver).getCapabilities();
        Path references = Paths.get(cfg.referencesBaseDir())
                .resolve(getPlatform(capabilities))
                .resolve(capabilities.getBrowserName().toLowerCase().replaceAll(" ", "_"))
                .resolve(capabilities.getBrowserVersion())
                .resolve(testClass.replaceAll("\\.", "/"))
                .resolve(testMethod);
        if (parametrizedTestId.isPresent()) {
            references = references.resolve(parametrizedTestId.get());
        }
        return references;
    }

    private String getPlatform(Capabilities capabilities) {
        return capabilities.getCapability(PLATFORM_NAME).toString().toLowerCase();
    }
}
