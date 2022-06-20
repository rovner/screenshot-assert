package com.github.rovner.screenshot.assertions.core.ignoring;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class AreaIgnoringTest {

    private final Rectangle rect1 = new Rectangle(0, 0, 10, 10);
    private final Rectangle rect2 = new Rectangle(20, 20, 30, 40);

    @Test
    void shouldReturnAreas() {
        Set<Rectangle> areas = AreaIgnoring.getIgnoredAreas(asList(
                new BasicAreaIgnoring(singletonList(rect1)),
                new BasicAreaIgnoring(singletonList(rect2)),
                new BasicHashIgnoring(singletonList(123))
        ));
        assertThat(areas).isEqualTo(Sets.newHashSet(asList(rect1, rect2)));
    }
}
