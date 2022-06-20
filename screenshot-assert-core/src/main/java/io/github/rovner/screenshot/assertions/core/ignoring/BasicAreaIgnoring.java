package io.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.Rectangle;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Ignores diff based on coordinates.
 */
public class BasicAreaIgnoring implements AreaIgnoring {

    private final Set<Rectangle> areas = new HashSet<>();

    BasicAreaIgnoring(Collection<Rectangle> areas) {
        this.areas.addAll(areas);
    }

    @Override
    public Set<Rectangle> getIgnoredAreas() {
        return areas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicAreaIgnoring that = (BasicAreaIgnoring) o;

        return areas.equals(that.areas);
    }

    @Override
    public int hashCode() {
        return areas.hashCode();
    }


}
