package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.cropper.DefaultImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.exceptions.SoftAssertionError;
import io.github.rovner.screenshot.assertions.core.platform.DesktopBrowserScreenshooter;
import io.github.rovner.screenshot.assertions.core.platform.PlatformScreenshoter;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.scaler.DefaultImageScaler;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import io.github.rovner.screenshot.assertions.core.soft.DefaultSoftExceptionCollector;
import io.github.rovner.screenshot.assertions.core.soft.SoftExceptionCollector;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.time.Duration.ofMillis;
import static java.util.Objects.requireNonNull;

/**
 * Screenshot assertion builder.
 */
public class ScreenshotAssertBuilder {
    private WebDriver webDriver;
    private ImageDiffer imageDiffer = new DefaultImageDiffer();
    private AllureListener allureListener = new DefaultAllureListener();
    private ReferenceStorage referenceStorage;
    private ImageCropper imageCropper = new DefaultImageCropper();
    private ImageScaler imageScaler = new DefaultImageScaler();
    private Map<String, PlatformScreenshoter> platformScreenshoters = getDefaultPlatformScreenshooters();

    private ScreenshotAssertConfig cfg = ConfigFactory.create(ScreenshotAssertConfig.class);

    private SoftExceptionCollector softExceptionCollector = new DefaultSoftExceptionCollector();

    private boolean isSoft = false;

    private ScreenshotAssertBuilder() {
    }

    /**
     * Creates new screenshot assertion builder.
     *
     * @return new builder
     */
    public static ScreenshotAssertBuilder builder() {
        return new ScreenshotAssertBuilder();
    }

    public ScreenshotAssertBuilder setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        return this;
    }

    public ScreenshotAssertBuilder setReferenceStorage(ReferenceStorage referenceStorage) {
        this.referenceStorage = referenceStorage;
        return this;
    }

    public ScreenshotAssertBuilder setImageDiffer(ImageDiffer imageDiffer) {
        this.imageDiffer = imageDiffer;
        return this;
    }

    public ScreenshotAssertBuilder setImageCropper(ImageCropper imageCropper) {
        this.imageCropper = imageCropper;
        return this;
    }

    public ScreenshotAssertBuilder setAllureListener(AllureListener allureListener) {
        this.allureListener = allureListener;
        return this;
    }

    public ScreenshotAssertBuilder setSoft(boolean isSoft) {
        this.isSoft = isSoft;
        return this;
    }

    public ScreenshotAssertBuilder setConfig(ScreenshotAssertConfig config) {
        this.cfg = config;
        return this;
    }

    public ScreenshotAssertBuilder setSoftExceptionsCollector(SoftExceptionCollector collector) {
        this.softExceptionCollector = collector;
        return this;
    }

    public ScreenshotAssertBuilder setImageScaler(ImageScaler imageScaler) {
        this.imageScaler = imageScaler;
        return this;
    }

    public ScreenshotAssertBuilder setPlatformScreenshoters(Map<String, PlatformScreenshoter> platformScreenshoters) {
        this.platformScreenshoters = platformScreenshoters;
        return this;
    }

    public ScreenshotAssertBuilder addPlatformScreenshooter(String id, PlatformScreenshoter platformScreenshoter) {
        this.platformScreenshoters.put(id, platformScreenshoter);
        return this;
    }

    /**
     * Build new screenshot assertion
     *
     * @param screenshot to compare
     * @return new assertion
     */
    public ScreenshotAssert build(Screenshot screenshot) {
        requireNonNull(webDriver, "Web driver can not be null");
        requireNonNull(referenceStorage, "References reader can not be null");
        return new ScreenshotAssert(
                webDriver,
                referenceStorage,
                imageDiffer,
                imageCropper,
                imageScaler,
                new ArrayList<>(platformScreenshoters.values()),
                allureListener,
                isSoft,
                softExceptionCollector,
                cfg,
                screenshot
        );
    }

    /**
     * Build new screenshot assertion. Syntax sugar for {@link #build(Screenshot)}
     *
     * @param screenshot to compare
     * @return new assertion
     */
    public ScreenshotAssert assertThat(Screenshot screenshot) {
        return this.build(screenshot);
    }

    /**
     * Collects all exceptions from soft assertions and throws {@link SoftAssertionError} if there are any.
     */
    public void assertAll() {
        if (!isSoft && !cfg.isSoft()) {
            throw new IllegalStateException(String.format("%s.assertAll supposed to be called only when isSoft=true",
                    this.getClass().getCanonicalName()));
        }
        Collection<Throwable> exceptions = softExceptionCollector.getAll();
        if (!exceptions.isEmpty()) {
            SoftAssertionError error = new SoftAssertionError(String.format("%d assertion(s) failed", exceptions.size()));
            exceptions.forEach(error::addSuppressed);
            throw error;
        }
    }

    /**
     * @return default platform screenshooters.
     */
    public static Map<String, PlatformScreenshoter> getDefaultPlatformScreenshooters() {
        Map<String, PlatformScreenshoter> screenshoters = new HashMap<>();
        screenshoters.put("desktop", new DesktopBrowserScreenshooter(ofMillis(0)));
        return screenshoters;
    }
}
