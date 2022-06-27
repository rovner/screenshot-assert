package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.screenshot.ScreenshotConfiguration;
import io.github.rovner.screenshot.assertions.core.soft.DefaultSoftExceptionCollector;
import io.github.rovner.screenshot.assertions.core.soft.SoftExceptionCollector;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import java.awt.*;

import static java.awt.Color.MAGENTA;

@Data
@Builder(toBuilder = true)
public class ScreenshotAssertionConfiguration {
    private ReferenceStorage referenceStorage;
    @Default
    private ScreenshotConfiguration screenshotConfiguration = ScreenshotConfiguration.builder().build();
    @Default
    private ImageDiffer imageDiffer = new DefaultImageDiffer();
    @Default
    private AllureListener allureListener = new DefaultAllureListener();
    @Default
    private boolean isSoft = false;
    @Default
    private SoftExceptionCollector softExceptionCollector = new DefaultSoftExceptionCollector();
    @Default
    private Color contextMarkColor = MAGENTA;

}
