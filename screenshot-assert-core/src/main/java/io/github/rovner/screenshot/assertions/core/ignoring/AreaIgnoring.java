package io.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.Rectangle;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Common interface for ignorings based on area.
 */
public interface AreaIgnoring extends Ignoring {
    /**
     * Returns area that should be ignored.
     *
     * @return ignored areas.
     */
    Set<Rectangle> getIgnoredAreas();

    /**
     * Filter area ignorings and map to areas.
     *
     * @param ignorings all ignorings.
     * @return areas to ignore.
     */
    static Set<Rectangle> getIgnoredAreas(Collection<Ignoring> ignorings) {
        return ignorings.stream()
                .filter(ignoring -> ignoring instanceof AreaIgnoring)
                .map(ignoring -> (AreaIgnoring) ignoring)
                .flatMap(ignoring -> ignoring.getIgnoredAreas().stream())
                .collect(toSet());
    }
}
