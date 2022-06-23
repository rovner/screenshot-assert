package io.github.rovner.screenshot.assertions.core.soft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default exception collector implementation.
 */
public class DefaultSoftExceptionCollector implements SoftExceptionCollector {

    private final List<Throwable> throwables = new ArrayList<>();

    @Override
    public void add(Throwable throwable) {
        throwables.add(throwable);
    }

    @Override
    public Collection<Throwable> getAll() {
        return throwables;
    }
}
