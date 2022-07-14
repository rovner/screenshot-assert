package io.github.rovner.screenshot.assertions.core.dpr;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

/**
 * Dpr detector with fixed dpr
 */
public class FixedDprDetector implements DprDetector{

    private final int dpr;

    FixedDprDetector(int dpr) {
        this.dpr = dpr;
    }

    /**
     * Create new fixed dpr detector.
     * @param dpr fixed dpr.
     * @return new detector.
     */
    public static FixedDprDetector fixed(int dpr) {
        return new FixedDprDetector(dpr);
    }

    @Override
    public double detect(WebDriverWrapper webDriver) {
        return dpr;
    }
}
