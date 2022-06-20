package io.rovner.screenshot.assertions.junit;

import io.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.rovner.screenshot.assertions.core.allure.AllureListener;
import io.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * Screenshot assert junit 5 extension.
 */
public class ScreenshotAssertExtension implements BeforeEachCallback {

    private Path references;
    private final Supplier<WebDriver> webDriverSupplier;
    private ImageDiffer imageDiffer = new DefaultImageDiffer();
    private AllureListener allureListener = new DefaultAllureListener();

    public ScreenshotAssertExtension(Supplier<WebDriver> webDriverSupplier) {
        this.webDriverSupplier = webDriverSupplier;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        String referencesBaseDir = System.getProperty(
                "io.rovner.screenshot.assert.references.base.dir",
                "src/test/resources/references");
        references = Paths.get(referencesBaseDir)
                .resolve(context.getRequiredTestClass().getCanonicalName())
                .resolve(context.getRequiredTestMethod().getName());

        if (context.getUniqueId().contains("#")) {
            String caseNumber = context.getUniqueId()
                    .replaceAll(".*#", "")
                    .replace("]", "");
            references = references.resolve(caseNumber);
        }
    }

    /**
     * Sets image differ to be used in screenshot comparison.
     *
     * @param imageDiffer differ to use.
     * @return self.
     */
    public ScreenshotAssertExtension withImageDiffer(ImageDiffer imageDiffer) {
        this.imageDiffer = imageDiffer;
        return this;
    }

    /**
     * Sets allure listener to be used for reporting.
     *
     * @param allureListener listener to use.
     * @return self.
     */
    public ScreenshotAssertExtension withAllureListener(AllureListener allureListener) {
        this.allureListener = allureListener;
        return this;
    }

    /**
     * Build and return screenshot assert.
     *
     * @param screenshot to take.
     * @return assert.
     */
    public ScreenshotAssert assertThat(Screenshot screenshot) {
        return ScreenshotAssert.builder()
                .webDriver(webDriverSupplier.get())
                .screenshot(screenshot)
                .references(references)
                .imageDiffer(imageDiffer)
                .allureListener(allureListener)
                .build();
    }
}
