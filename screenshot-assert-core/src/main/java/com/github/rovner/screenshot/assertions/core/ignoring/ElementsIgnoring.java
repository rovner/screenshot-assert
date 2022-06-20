package com.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Ignores diff based on coordinates from elements.
 */
public class ElementsIgnoring implements AreaIgnoring {

    private final List<WebElement> elements = new ArrayList<>();
    private BasicAreaIgnoring delegate;

    public ElementsIgnoring(Collection<WebElement> elements) {
        this.elements.addAll(elements);
    }

    @Override
    public Set<Rectangle> getIgnoredAreas() {
        if (delegate == null) {
            List<Rectangle> areas = elements.stream()
                    .map(WebElement::getRect)
                    .collect(toList());
            delegate = new BasicAreaIgnoring(areas);
        }
        return delegate.getIgnoredAreas();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementsIgnoring that = (ElementsIgnoring) o;

        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
