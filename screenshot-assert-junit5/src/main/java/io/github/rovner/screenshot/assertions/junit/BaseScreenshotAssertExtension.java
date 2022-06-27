package io.github.rovner.screenshot.assertions.junit;

import io.github.rovner.screenshot.assertions.core.*;
import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.cropper.DefaultImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.DesktopViewportCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.dpr.DefaultDprDetector;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import io.github.rovner.screenshot.assertions.core.reference.DefaultReferenceStorage;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.scaler.DefaultImageScaler;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base screenshot assert junit 5 extension.
 */
public abstract class BaseScreenshotAssertExtension<T extends BaseScreenshotAssertExtension<T>> implements BeforeEachCallback {

    private String testClass;
    private String testMethod;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> parametrizedTestId;
    private final Supplier<WebDriver> webDriverSupplier;
    protected final ScreenshotAssertionConfiguration configuration = ScreenshotAssertionConfiguration.builder().build();
    private ScreenshotAssertions screenshotAssertions;

    protected BaseScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
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
     * Default: {@link DefaultImageDiffer}.
     *
     * @param imageDiffer differ to use.
     * @return self.
     */
    public T imageDiffer(ImageDiffer imageDiffer) {
        this.configuration.setImageDiffer(imageDiffer);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets reference storage to be used.
     * Default: {@link DefaultReferenceStorage}.
     *
     * @param referenceStorage reference storage to use.
     * @return self.
     */
    public T referenceStorage(ReferenceStorage referenceStorage) {
        this.configuration.setReferenceStorage(referenceStorage);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets allure listener to be used.
     * Default: {@link DefaultAllureListener}.
     *
     * @param allureListener allure listener to use.
     * @return self.
     */
    public T allureListener(AllureListener allureListener) {
        this.configuration.setAllureListener(allureListener);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets image scaler to be used.
     * Default: {@link DefaultImageScaler}.
     *
     * @param imageScaler image scaler to use.
     * @return self.
     */
    public T imageScaler(ImageScaler imageScaler) {
        this.configuration.getScreenshotConfiguration().setImageScaler(imageScaler);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets device pixel ratio detector to be used.
     * Default: {@link DefaultDprDetector}.
     *
     * @param dprDetector dpr detector to use.
     * @return self.
     */
    public T dprDetector(DprDetector dprDetector) {
        this.configuration.getScreenshotConfiguration().setDprDetector(dprDetector);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets timeout to sleep after scroll in whole page screenshot.
     * Default: {@code ofMillis(0)}.
     *
     * @param scrollSleepTimeout scroll sleep timeout.
     * @return self.
     */
    public T scrollSleepTimeout(Duration scrollSleepTimeout) {
        this.configuration.getScreenshotConfiguration().setScrollSleepTimeout(scrollSleepTimeout);
        //noinspection unchecked
        return (T) this;
    }


    /**
     * Sets margin (in pixels) that will be cut from top and bottom of screenshots in whole page screenshot.
     * Default: {@code 0}.
     *
     * @param scrollMarginPixels margin in pixels.
     * @return self.
     */
    public T scrollMarginPixels(int scrollMarginPixels) {
        this.configuration.getScreenshotConfiguration().setScrollMarginPixels(scrollMarginPixels);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets image cropper to be used.
     * Default: {@link DefaultImageCropper}
     *
     * @param imageCropper image cropper.
     * @return self.
     */
    public T imageCropper(ImageCropper imageCropper) {
        this.configuration.getScreenshotConfiguration().setImageCropper(imageCropper);
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets viewport cropper to be used.
     * Default: {@link DesktopViewportCropper}
     *
     * @param viewportCropper cropper to use.
     * @return self.
     */
    public T viewportCropper(ViewportCropper viewportCropper) {
        this.configuration.getScreenshotConfiguration().setViewportCropper(viewportCropper);
        //noinspection unchecked
        return (T) this;
    }


    /**
     * Build and return screenshot assert.
     *
     * @param screenshot to take.
     * @return assert.
     */
    public abstract ScreenshotAssertion assertThat(Screenshot screenshot);

    protected ScreenshotAssertions getScreenshotAssertions() {
        if (screenshotAssertions == null) {
            WebDriver webDriver = webDriverSupplier.get();
            if (configuration.getReferenceStorage() == null) {
                DefaultReferenceStorage storage = new DefaultReferenceStorage(resolveReferencesPath(webDriver));
                configuration.setReferenceStorage(storage);
            }
            screenshotAssertions = new ScreenshotAssertions(webDriver, configuration);
        }
        return screenshotAssertions;
    }

    private Path resolveReferencesPath(WebDriver webDriver) {
        ScreenshotAssertionProperties cfg = ConfigFactory.create(ScreenshotAssertionProperties.class);
        WebDriverWrapper wrapper = new WebDriverWrapper(webDriver);
        Path references = Paths.get(cfg.referencesBaseDir())
                .resolve(wrapper.getPlatform().toString().toLowerCase())
                .resolve(wrapper.getBrowserName().toLowerCase().replaceAll(" ", "_"))
                .resolve(wrapper.getBrowserVersion())
                .resolve(testClass.replaceAll("\\.", "/"))
                .resolve(testMethod);
        if (parametrizedTestId.isPresent()) {
            references = references.resolve(parametrizedTestId.get());
        }
        return references;
    }
}
