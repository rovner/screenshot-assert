package io.github.rovner.screenshot.assertions.core.soft;

import java.util.Collection;

/**
 * Collects exceptions from soft assertions.
 */
public interface SoftExceptionCollector {

    /**
     * Save exception from soft assertion
     *
     * @param throwable exception to save
     */
    void add(Throwable throwable);

    /**
     * Return all saved exceptions
     *
     * @return all exceptions
     */
    Collection<Throwable> getAll();
}
