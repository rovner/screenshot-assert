package io.github.rovner.screenshot.assertions.core.cropper;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Viewport croppers.
 */
public class ViewportCroppers {

    private ViewportCroppers() {
    }

    /**
     * Creates new desktop viewport cropper.
     *
     * @return new desktop cropper.
     */
    public static ViewportCropper desktop() {
        return new DesktopViewportCropper();
    }

    /**
     * Creates new matching viewport cropper.
     *
     * @return new matching cropper.
     */
    public static MatchingViewportCropper matching() {
        return new MatchingViewportCropper();
    }

    /**
     * Creates new aggregation viewport cropper.
     *
     * @param croppers croppers to be applied.
     * @return new aggregating cropper.
     */
    public static ViewportCropper aggregating(List<ViewportCropper> croppers) {
        return new AggregatingViewportCropper(croppers);
    }

    /**
     * Creates new aggregation viewport cropper.
     *
     * @param croppers croppers to be applied.
     * @return new aggregating cropper.
     */
    public static ViewportCropper aggregating(ViewportCropper... croppers) {
        return aggregating(asList(croppers));
    }

    /**
     * Creates new cropper that cuts fixed footer from screenshot.
     *
     * @param footerHeight height to cut (pixels * dpr).
     * @return new footer cutting cropper.
     */
    public static ViewportCropper fixedFooterCutting(int footerHeight) {
        return new FixedFooterCuttingViewportCropper(footerHeight);
    }

    /**
     * Creates new cropper that cuts footer from screenshot.
     *
     * @return new floating footer cutting cropper.
     */
    public static ViewportCropper floatingFooterCutting() {
        return new FloatingFooterCuttingViewportCropper();
    }

    /**
     * Creates new cropper that cuts fixed header from screenshot.
     *
     * @param headerHeight height to cut (pixels * dpr).
     * @return new header cutting cropper.
     */
    public static ViewportCropper fixedHeaderCutting(int headerHeight) {
        return new FixedHeaderCuttingViewportCropper(headerHeight);
    }

    /**
     * Creates new cropper that cuts header from screenshot.
     *
     * @return new floating header cutting cropper.
     */
    public static ViewportCropper floatingHeaderCutting() {
        return new FloatingHeaderCuttingViewportCropper();
    }

    /**
     * Creates new cropper that cuts viewport based on web driver capabilities.
     *
     * @return new capabilities cropper.
     */
    public static ViewportCropper capabilities() {
        return new CapabilitiesViewportCropper();
    }
}
