package io.github.rovner.screenshot.assertions.core.ignoring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementFoundByIgnoringTest {

    @Mock
    WebElement webElement1;
    @Mock
    WebElement webElement2;
    @Mock
    WebDriver webDriver;

    private final Rectangle rectangle1 = new Rectangle(0, 0, 10, 10);
    private final Rectangle rectangle2 = new Rectangle(10, 10, 10, 10);
    private final By selector1 = By.cssSelector(".test1");
    private final By selector2 = By.cssSelector(".test2");

    @Test
    void shouldReturnArea1() {
        mockDriverResponses();
        ElementsFoundByIgnoring ignoring = Ignorings.elementsBy(selector1, selector2);
        ignoring.initWebDriver(webDriver);
        assertThat(ignoring.getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnArea2() {
        mockDriverResponses();
        ElementsFoundByIgnoring ignoring = Ignorings.elementsBy(asList(selector1, selector2));
        ignoring.initWebDriver(webDriver);
        assertThat(ignoring.getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnHashCode() {
        Assertions.assertThat(Ignorings.elementsBy(selector1).hashCode())
                .isEqualTo(741768738);
    }

    @Test
    void shouldCompareEqualObjects() {
        Assertions.assertThat(Ignorings.elementsBy(selector1).equals(Ignorings.elementsBy(selector1)))
                .isTrue();
    }

    @Test
    void shouldCompareNonEqualObjects() {
        Assertions.assertThat(Ignorings.elementsBy(selector1).equals(Ignorings.elementsBy(selector2)))
                .isFalse();
    }

    private void mockDriverResponses() {
        when(webDriver.findElements(selector1)).thenReturn(singletonList(webElement1));
        when(webDriver.findElements(selector2)).thenReturn(singletonList(webElement2));
        when(webElement1.getRect()).thenReturn(rectangle1);
        when(webElement2.getRect()).thenReturn(rectangle2);
    }
}
