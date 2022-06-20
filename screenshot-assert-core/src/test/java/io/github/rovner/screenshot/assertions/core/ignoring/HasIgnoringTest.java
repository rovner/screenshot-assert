package io.github.rovner.screenshot.assertions.core.ignoring;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class HasIgnoringTest {

    @Test
    void shouldReturnAreas() {
        Set<Integer> areas = HashIgnoring.getIgnoredHashes(asList(
                new BasicAreaIgnoring(singletonList(new Rectangle(0, 0, 10, 10))),
                new BasicHashIgnoring(singletonList(123)),
                new BasicHashIgnoring(singletonList(321))
        ));
        Assertions.assertThat(areas).isEqualTo(Sets.newHashSet(asList(123, 321)));
    }
}
