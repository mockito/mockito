/*
 * Copyright (C) 2016 Mockito contributors.
 *
 * Licensed under the Apache License, Version 2.0.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.Incubating;

/**
 * Wraps an other matcher displaying an user defined descriptionOnFailure in a case of failure.
 *
 * <p>
 * Potentially useful in conjunction with inline lambda predicate to provide a simple custom answer logic.
 * </p>
 *
 * <pre class="code"><code class="java">
 * {@literal @}Test
 * public void shouldAllowToUseLambdaInStubbing() {
 *     //given
 *     given(ts.findNumberOfShipsInRangeByCriteria(argLambda(c -> c.getMinimumRange() > 1000))).willReturn(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(1500, 2))).isEqualTo(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(700, 2))).isEqualTo(0);
 * }
 * </code></pre>
 *
 * @param <T> type of argument
 *
 * @author Marcin Zajączkowski
 * @since 3.0.0
 */
@Incubating
public class DescriptableWrapperMatcher<T> implements ArgumentMatcher<T> {

    private final ArgumentMatcher<T> wrappedMatcher;
    private final String descriptionOnFailure;

    public DescriptableWrapperMatcher(ArgumentMatcher<T> wrappedMatcher, String descriptionOnFailure) {
        this.wrappedMatcher = wrappedMatcher;
        this.descriptionOnFailure = descriptionOnFailure;
    }

    @Override
    public boolean matches(T item) {
        return wrappedMatcher.matches(item);
    }

    @Override
    public String toString() {
        return descriptionOnFailure;
    }
}
