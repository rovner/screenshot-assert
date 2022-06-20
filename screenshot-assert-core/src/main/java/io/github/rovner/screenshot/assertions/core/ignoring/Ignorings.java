package io.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Ignorings factory.
 */
public final class Ignorings {

    public static BasicAreaIgnoring area(int x, int y, int width, int height) {
        return area(new Rectangle(x, y, height, width));
    }

    public static BasicAreaIgnoring area(Rectangle rectangle) {
        return areas(singletonList(rectangle));
    }

    public static BasicAreaIgnoring areas(Collection<Rectangle> rectangles) {
        return new BasicAreaIgnoring(rectangles);
    }

    public static BasicAreaIgnoring areas(Rectangle... rectangles) {
        return areas(asList(rectangles));
    }

    public static BasicHashIgnoring hash(int diffHash) {
        return hashes(singletonList(diffHash));
    }

    public static BasicHashIgnoring hashes(Collection<Integer> diffHashes) {
        return new BasicHashIgnoring(diffHashes);
    }

    public static BasicHashIgnoring hashes(Integer... diffHashes) {
        return hashes(asList(diffHashes));
    }

    public static ElementsIgnoring element(WebElement element) {
        return elements(singletonList(element));
    }

    public static ElementsIgnoring elements(Collection<WebElement> elements) {
        return new ElementsIgnoring(elements);
    }

    public static ElementsIgnoring elements(WebElement... elements) {
        return new ElementsIgnoring(asList(elements));
    }

    public static ElementsFoundByIgnoring elementsBy(Collection<By> selectors) {
        return new ElementsFoundByIgnoring(selectors);
    }

    public static ElementsFoundByIgnoring elementsBy(By... selectors) {
        return elementsBy(asList(selectors));
    }
}
