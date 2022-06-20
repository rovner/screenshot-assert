package io.github.rovner.screenshot.assertions.core.ignoring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Ignores diff based on diff hash code.
 */
public class BasicHashIgnoring implements HashIgnoring {

    private final Set<Integer> diffHashes = new HashSet<>();

    BasicHashIgnoring(Collection<Integer> diffHashes) {
        this.diffHashes.addAll(diffHashes);
    }

    @Override
    public Collection<Integer> getIgnoredHashes() {
        return diffHashes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicHashIgnoring that = (BasicHashIgnoring) o;

        return diffHashes.equals(that.diffHashes);
    }

    @Override
    public int hashCode() {
        return diffHashes.hashCode();
    }
}
