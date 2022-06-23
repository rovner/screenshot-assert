package io.github.rovner.screenshot.assertions.core.ignoring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementIgnoringTest {

    @Mock
    private WebElement webElement1;
    @Mock
    private WebElement webElement2;

    private final Rectangle rectangle1 = new Rectangle(0, 0, 10, 10);
    private final Rectangle rectangle2 = new Rectangle(10, 10, 100, 10);

    @Test
    @DisplayName("Should return ignored areas for element")
    void shouldReturnArea1() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        ElementsIgnoring ignoring = element(webElement1);
        ignoring.getIgnoredAreas();//to yest lazy initialization
        assertThat(ignoring.getIgnoredAreas())
                .isEqualTo(newHashSet(singletonList(rectangle1)));
    }

    @Test
    @DisplayName("Should return ignored areas for elements as vararg")
    void shouldReturnArea2() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        when(webElement2.getRect()).thenReturn(rectangle2);
        assertThat(elements(webElement1, webElement2).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    @DisplayName("Should return ignored areas for elements as list")
    void shouldReturnArea3() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        when(webElement2.getRect()).thenReturn(rectangle2);
        assertThat(elements(asList(webElement1, webElement2)).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    @DisplayName("Should return hash code")
    void shouldReturnHashCode() {
        assertThat(element(webElement1).hashCode())
                .isNotNull();
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    @DisplayName("Should return return true for equal objects")
    void shouldCompareEqualObjects() {
        ElementsIgnoring element = element(webElement1);
        assertThat(element.equals(elements(webElement1)))
                .isTrue();
        assertThat(element.equals(element))
                .isTrue();
    }

    @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "ConstantConditions"})
    @Test
    @DisplayName("Should return false for non equal objects")
    void shouldCompareNonEqualObjects() {
        assertThat(element(webElement1).equals(elements(webElement2)))
                .isFalse();
        assertThat(element(webElement1).equals(""))
                .isFalse();
        assertThat(element(webElement1).equals(null))
                .isFalse();
    }
}
