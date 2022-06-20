package com.github.rovner.screenshot.assertions.core.screenshot;

/**
 * Screenshot description representation, i.e. whole page or some element.
 */
public interface ScreenshotDescription {
    /**
     * Returns screenshot description, i.e. whole page or some element.
     *
     * @return screenshot description.
     */
    String describe();
}
