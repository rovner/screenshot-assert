package io.github.rovner.screenshot.assertions.core.dpr;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;

/**
 * Detects device pixel ratio.
 */
public interface DprDetector {

    /**
     * Detects device pixel ratio.
     *
     * @param webDriver web driver
     * @return device pixel ratio.
     */
    double detect(WebDriverWrapper webDriver);
}
