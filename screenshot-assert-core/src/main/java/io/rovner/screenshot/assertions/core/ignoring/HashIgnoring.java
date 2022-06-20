package io.rovner.screenshot.assertions.core.ignoring;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Common interface for ignorings based on diff hash code.
 */
public interface HashIgnoring extends Ignoring {
    /**
     * Returns hash codes to be ignored.
     *
     * @return hash codes to be ignored.
     */
    Collection<Integer> getIgnoredHashes();

    /**
     * Filter hash code ignorings and map to hash codes.
     *
     * @param ignorings hash code ignorings.
     * @return hash codes.
     */
    static Set<Integer> getIgnoredHashes(Collection<Ignoring> ignorings) {
        return ignorings.stream()
                .filter(ignoring -> ignoring instanceof HashIgnoring)
                .map(ignoring -> (HashIgnoring) ignoring)
                .flatMap(ignoring -> ignoring.getIgnoredHashes().stream())
                .collect(toSet());
    }
}
