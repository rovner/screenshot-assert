package com.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Ignores diff based on coordinates from elements.
 */
public class ElementsFoundByIgnoring implements AreaIgnoring, WebDriverInit {

    private final Set<By> selectors = new HashSet<>();
    private ElementsIgnoring delegate;
    private WebDriver webDriver;

    public ElementsFoundByIgnoring(Collection<By> selectors) {
        this.selectors.addAll(selectors);
    }

    @Override
    public Set<Rectangle> getIgnoredAreas() {
        if (delegate == null) {
            List<WebElement> elements = selectors.stream()
                    .flatMap(selector -> webDriver.findElements(selector).stream())
                    .collect(toList());
            delegate = new ElementsIgnoring(elements);
        }
        return delegate.getIgnoredAreas();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementsFoundByIgnoring that = (ElementsFoundByIgnoring) o;

        return selectors.equals(that.selectors);
    }

    @Override
    public int hashCode() {
        return selectors.hashCode();
    }

    @Override
    public void initWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}
