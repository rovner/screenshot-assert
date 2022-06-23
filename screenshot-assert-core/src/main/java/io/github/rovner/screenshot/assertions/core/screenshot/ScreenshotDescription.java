package io.github.rovner.screenshot.assertions.core.screenshot;

/**
 * Screenshot description representation, i.e. viewport or some element.
 */
public interface ScreenshotDescription {
    /**
     * Returns screenshot description, i.e. viewport or some element.
     *
     * @return screenshot description.
     */
    String describe();
}
