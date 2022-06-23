package io.github.rovner.screenshot.assertions.core.ignoring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.hash;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.hashes;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;

public class BasicHashIgnoringTest {

    @Test
    @DisplayName("Should return ignored hashes as varargs")
    void shouldReturnHashes1() {
        BasicHashIgnoring ignoring = hashes(1, 2, 3);
        ignoring.getIgnoredHashes();//to test lazy initialization
        assertThat(ignoring.getIgnoredHashes())
                .isEqualTo(newHashSet(asList(1, 2, 3)));
    }

    @Test
    @DisplayName("Should return ignored hash")
    void shouldReturnHashes2() {
        assertThat(hash(1).getIgnoredHashes())
                .isEqualTo(newHashSet(singletonList(1)));
    }

    @Test
    @DisplayName("Should return ignored hashes as list")
    void shouldReturnHashes3() {
        assertThat(hashes(asList(1, 2, 3)).getIgnoredHashes())
                .isEqualTo(newHashSet(asList(1, 2, 3)));
    }

    @Test
    @DisplayName("Should return hash code")
    void shouldReturnHashCode() {
        assertThat(hash(1).hashCode())
                .isEqualTo(1);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    @DisplayName("Should return return true for equal objects")
    void shouldCompareEqualObjects() {
        BasicHashIgnoring ignoring = hash(1);
        assertThat(ignoring.equals(hash(1)))
                .isTrue();
        assertThat(ignoring.equals(ignoring))
                .isTrue();
    }

    @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "ConstantConditions"})
    @Test
    @DisplayName("Should return false for non equal objects")
    void shouldCompareNonEqualObjects() {
        assertThat(hash(1).equals(hash(2)))
                .isFalse();
        assertThat(hash(1).equals(""))
                .isFalse();
        assertThat(hash(1).equals(null))
                .isFalse();
    }
}
