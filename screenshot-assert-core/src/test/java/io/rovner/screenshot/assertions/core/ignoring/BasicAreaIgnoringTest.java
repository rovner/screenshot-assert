package io.rovner.screenshot.assertions.core.ignoring;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import static io.rovner.screenshot.assertions.core.ignoring.Ignorings.area;
import static io.rovner.screenshot.assertions.core.ignoring.Ignorings.areas;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;

public class BasicAreaIgnoringTest {

    private final Rectangle rectangle1 = new Rectangle(0, 0, 10, 10);
    private final Rectangle rectangle2 = new Rectangle(10, 10, 10, 10);

    @Test
    void shouldReturnAreas1() {
        assertThat(areas(asList(rectangle1, rectangle2)).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnAreas2() {
        assertThat(area(0, 0, 10, 10).getIgnoredAreas()).
                isEqualTo(newHashSet(singletonList(rectangle1)));
    }

    @Test
    void shouldReturnAreas3() {
        assertThat(areas(rectangle1, rectangle2).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnHashCode() {
        assertThat(area(rectangle1).hashCode())
                .isEqualTo(31072);
    }

    @Test
    void shouldCompareEqualObjects() {
        assertThat(area(rectangle1).equals(area(rectangle1)))
                .isTrue();
    }

    @Test
    void shouldCompareNonEqualObjects() {
        assertThat(area(rectangle1).equals(area(rectangle2)))
                .isFalse();
    }
}
