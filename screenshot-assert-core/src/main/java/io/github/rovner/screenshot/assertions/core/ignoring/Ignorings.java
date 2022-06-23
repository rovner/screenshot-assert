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

    private Ignorings() {
    }

    /**
     * Creates area ignoring.
     *
     * @param x      x coordinate of ignored area.
     * @param y      y coordinate of ignored area.
     * @param width  width of ignored area.
     * @param height height of ignored area.
     * @return new area ignoring object.
     */
    public static BasicAreaIgnoring area(int x, int y, int width, int height) {
        return area(new Rectangle(x, y, height, width));
    }

    /**
     * Creates area ignoring.
     * @param rectangle rectangle to ignore.
     * @return new area ignoring object.
     */
    public static BasicAreaIgnoring area(Rectangle rectangle) {
        return areas(singletonList(rectangle));
    }

    /**
     * Creates areas ignoring.
     * @param rectangles rectangles to ignore
     * @return new areas ignoring object.
     */
    public static BasicAreaIgnoring areas(Collection<Rectangle> rectangles) {
        return new BasicAreaIgnoring(rectangles);
    }

    /**
     * Creates areas ignoring.
     * @param rectangles rectangles to ignore
     * @return new areas ignoring object.
     */
    public static BasicAreaIgnoring areas(Rectangle... rectangles) {
        return areas(asList(rectangles));
    }

    /**
     * Creates diff hash ignoring.
     * @param diffHash diff hash to ignore.
     * @return new diff hash ignoring object.
     */
    public static BasicHashIgnoring hash(int diffHash) {
        return hashes(singletonList(diffHash));
    }

    /**
     * Creates diff hashes ignoring.
     * @param diffHashes diff hashes to ignore.
     * @return new diff hashes ignoring object.
     */
    public static BasicHashIgnoring hashes(Collection<Integer> diffHashes) {
        return new BasicHashIgnoring(diffHashes);
    }

    /**
     * Creates diff hashes ignoring.
     * @param diffHashes diff hashes to ignore.
     * @return new diff hashes ignoring object.
     */
    public static BasicHashIgnoring hashes(Integer... diffHashes) {
        return hashes(asList(diffHashes));
    }

    /**
     * Creates element ignoring.
     * @param element element to ignore.
     * @return new element ignoring.
     */
    public static ElementsIgnoring element(WebElement element) {
        return elements(singletonList(element));
    }

    /**
     * Creates elements ignoring.
     * @param elements elements to ignore.
     * @return new elements ignoring.
     */
    public static ElementsIgnoring elements(Collection<WebElement> elements) {
        return new ElementsIgnoring(elements);
    }

    /**
     * Creates elements ignoring.
     * @param elements elements to ignore.
     * @return new elements ignoring.
     */
    public static ElementsIgnoring elements(WebElement... elements) {
        return new ElementsIgnoring(asList(elements));
    }

    /**
     * Creates elements found  by selector ignoring.
     * @param selectors selectors to ignore.
     * @return new elements ignoring.
     */
    public static ElementsFoundByIgnoring elementsBy(Collection<By> selectors) {
        return new ElementsFoundByIgnoring(selectors);
    }

    /**
     * Creates elements found  by selector ignoring.
     * @param selectors selectors to ignore.
     * @return new elements ignoring.
     */
    public static ElementsFoundByIgnoring elementsBy(By... selectors) {
        return elementsBy(asList(selectors));
    }
}
