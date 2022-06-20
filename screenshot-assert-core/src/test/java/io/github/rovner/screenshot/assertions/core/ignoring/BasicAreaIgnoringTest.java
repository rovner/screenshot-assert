package io.github.rovner.screenshot.assertions.core.ignoring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.area;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.areas;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;

public class BasicAreaIgnoringTest {

    private final Rectangle rectangle1 = new Rectangle(0, 0, 10, 10);
    private final Rectangle rectangle2 = new Rectangle(10, 10, 10, 10);

    @Test
    void shouldReturnAreas1() {
        Assertions.assertThat(Ignorings.areas(asList(rectangle1, rectangle2)).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnAreas2() {
        Assertions.assertThat(Ignorings.area(0, 0, 10, 10).getIgnoredAreas()).
                isEqualTo(newHashSet(singletonList(rectangle1)));
    }

    @Test
    void shouldReturnAreas3() {
        Assertions.assertThat(Ignorings.areas(rectangle1, rectangle2).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    void shouldReturnHashCode() {
        Assertions.assertThat(Ignorings.area(rectangle1).hashCode())
                .isEqualTo(31072);
    }

    @Test
    void shouldCompareEqualObjects() {
        Assertions.assertThat(Ignorings.area(rectangle1).equals(Ignorings.area(rectangle1)))
                .isTrue();
    }

    @Test
    void shouldCompareNonEqualObjects() {
        Assertions.assertThat(Ignorings.area(rectangle1).equals(Ignorings.area(rectangle2)))
                .isFalse();
    }
}
