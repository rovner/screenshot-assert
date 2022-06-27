package io.github.rovner.screenshot.assertions.core.screenshot;

import io.github.rovner.screenshot.assertions.core.cropper.DefaultImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.cropper.ViewportCropper;
import io.github.rovner.screenshot.assertions.core.dpr.DefaultDprDetector;
import io.github.rovner.screenshot.assertions.core.dpr.DprDetector;
import io.github.rovner.screenshot.assertions.core.scaler.DefaultImageScaler;
import io.github.rovner.screenshot.assertions.core.scaler.ImageScaler;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

import java.time.Duration;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.desktop;
import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.matching;
import static io.github.rovner.screenshot.assertions.core.driver.WebDriverPredicates.isDesktop;

@Data
@Builder(toBuilder = true)
public class ScreenshotConfiguration {
    @Default
    private ImageCropper imageCropper = new DefaultImageCropper();
    @Default
    private ViewportCropper viewportCropper = matching()
            .match(isDesktop(), desktop());
    @Default
    private ImageScaler imageScaler = new DefaultImageScaler();
    @Default
    private DprDetector dprDetector = new DefaultDprDetector();
    @Default
    private Duration scrollSleepTimeout = Duration.ofMillis(0);
    @Default
    private int scrollMarginPixels = 0;
}
