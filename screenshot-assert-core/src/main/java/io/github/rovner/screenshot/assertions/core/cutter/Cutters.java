package io.github.rovner.screenshot.assertions.core.cutter;

/**
 * Cutters factory.
 */
public class Cutters {
    private Cutters() {
    }

    /**
     * Creates fixed header cutter.
     *
     * @param headerHeight header height to cut.
     * @return header cutter.
     */
    public static Cutter fixedHeader(int headerHeight) {
        return new FixedHeaderCutter(headerHeight);
    }

    /**
     * Creates floating header cutter.
     *
     * @return header cutter.
     */

    public static Cutter floatingHeader() {
        return new FloatingHeaderCutter();
    }

    /**
     * Creates floating footer cutter.
     *
     * @return footer cutter.
     */

    public static Cutter floatingFooter() {
        return new FloatingFooterCutter();
    }

    /**
     * Creates fixed footer cutter.
     *
     * @param footerHeight footer height to cut.
     * @return footer cutter.
     */
    public static Cutter fixedFooter(int footerHeight) {
        return new FixedFooterCutter(footerHeight);
    }
}
