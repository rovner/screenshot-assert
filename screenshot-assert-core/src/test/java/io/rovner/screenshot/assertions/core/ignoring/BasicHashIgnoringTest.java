package io.rovner.screenshot.assertions.core.ignoring;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;

public class BasicHashIgnoringTest {

    @Test
    void shouldReturnHashes1() {
        assertThat(Ignorings.hashes(1, 2, 3).getIgnoredHashes())
                .isEqualTo(newHashSet(asList(1, 2, 3)));
    }

    @Test
    void shouldReturnHashes2() {
        assertThat(Ignorings.hash(1).getIgnoredHashes())
                .isEqualTo(newHashSet(singletonList(1)));
    }

    @Test
    void shouldReturnHashes3() {
        assertThat(Ignorings.hashes(asList(1, 2, 3)).getIgnoredHashes())
                .isEqualTo(newHashSet(asList(1, 2, 3)));
    }

    @Test
    void shouldReturnHashCode() {
        assertThat(Ignorings.hash(1).hashCode())
                .isEqualTo(1);
    }

    @Test
    void shouldCompareEqualObjects() {
        assertThat(Ignorings.hash(1).equals(Ignorings.hash(1)))
                .isTrue();
    }

    @Test
    void shouldCompareNonEqualObjects() {
        assertThat(Ignorings.hash(1).equals(Ignorings.hash(2)))
                .isFalse();
    }
}
