package io.github.rovner.screenshot.assertions.core.ignoring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("Should return ignored multiple areas as list")
    void shouldReturnAreas1() {
        BasicAreaIgnoring ignoring = areas(asList(rectangle1, rectangle2));
        ignoring.getIgnoredAreas();//to test lazy initialization
        assertThat(ignoring.getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    @DisplayName("Should return ignored single area")
    void shouldReturnAreas2() {
        assertThat(area(0, 0, 10, 10).getIgnoredAreas()).
                isEqualTo(newHashSet(singletonList(rectangle1)));
    }

    @Test
    @DisplayName("Should return ignored multiple areas as varargs")
    void shouldReturnAreas3() {
        assertThat(areas(rectangle1, rectangle2).getIgnoredAreas())
                .isEqualTo(newHashSet(asList(rectangle1, rectangle2)));
    }

    @Test
    @DisplayName("Should return correct hash code")
    void shouldReturnHashCode() {
        assertThat(area(rectangle1).hashCode())
                .isEqualTo(31072);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    @DisplayName("Should return true for equal objects")
    void shouldCompareEqualObjects() {
        BasicAreaIgnoring ignoring = area(rectangle1);
        assertThat(ignoring.equals(area(rectangle1)))
                .isTrue();
        assertThat(ignoring.equals(ignoring))
                .isTrue();
    }

    @SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes"})
    @Test
    @DisplayName("Should return false for non equal objects")
    void shouldCompareNonEqualObjects() {
        assertThat(area(rectangle1).equals(area(rectangle2)))
                .isFalse();
        assertThat(area(rectangle1).equals(""))
                .isFalse();
        assertThat(area(rectangle1).equals(null))
                .isFalse();
    }
}
