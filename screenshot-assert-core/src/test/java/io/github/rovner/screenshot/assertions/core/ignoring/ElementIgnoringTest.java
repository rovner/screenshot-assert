package io.github.rovner.screenshot.assertions.core.ignoring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elements;
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
    void shouldReturnArea1() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        assertThat(Ignorings.element(webElement1).getIgnoredAreas())
                .isEqualTo(newHashSet(singletonList(rectangle1)));
    }

    @Test
    void shouldReturnArea2() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        when(webElement2.getRect()).thenReturn(rectangle2);
        assertThat(Ignorings.elements(webElement1, webElement2).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnArea3() {
        when(webElement1.getRect()).thenReturn(rectangle1);
        when(webElement2.getRect()).thenReturn(rectangle2);
        assertThat(Ignorings.elements(asList(webElement1, webElement2)).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldCompareEqualObjects() {
        assertThat(Ignorings.element(webElement1).equals(Ignorings.elements(webElement1)))
                .isTrue();
    }

    @Test
    void shouldCompareNonEqualObjects() {
        assertThat(Ignorings.element(webElement1).equals(Ignorings.elements(webElement2)))
                .isFalse();
    }
}
