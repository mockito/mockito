/*
 * Copyright (C) 2015 Mockito contributors.
 *
 * Licensed under the Apache License, Version 2.0.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.Incubating;

import java.util.function.Predicate;

/**
 * Allows creating inlined argument matcher with a lambda expression.
 * <p>
 * With Java 8 and lambda expressions ArgumentCaptor can be expressed inline:
 *
 * <pre class="code"><code class="java">
 *{@literal @}Test
 * public void shouldAllowToUseLambdaInStubbing() {
 *     //given
 *     given(ts.findNumberOfShipsInRangeByCriteria(argLambda(c -> c.getMinimumRange() > 1000))).willReturn(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(1500, 2))).isEqualTo(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(700, 2))).isEqualTo(0);
 * }
 * </code></pre>
 * <p>
 * In comparison the same logic implemented with a custom Answer in Java 7:
 *
 * <pre class="code"><code class="java">
 *{@literal @}Test
 * public void stubbingWithCustomAsnwerShouldBeLonger() {  //old way
 *     //given
 *     given(ts.findNumberOfShipsInRangeByCriteria(any())).willAnswer(new Answer<Integer>() {
 *        {@literal @}Override
 *         public Integer answer(InvocationOnMock invocation) throws Throwable {
 *             Object[] args = invocation.getArguments();
 *             ShipSearchCriteria criteria = (ShipSearchCriteria) args[0];
 *             if (criteria.getMinimumRange() > 1000) {
 *                 return 4;
 *             } else {
 *                 return 0;
 *             }
 *         }
 *     });
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(1500, 2))).isEqualTo(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(700, 2))).isEqualTo(0);
 * }
 * </code></pre>
 * <p>
 * Even Java 8 alone and using less readable constructions produce less compact code:
 *
 * <pre class="code"><code class="java">
 *{@literal @}Test
 * public void stubbingWithCustomAsnwerShouldBeLongerEvenAsLambda() {  //old way
 *     //given
 *     given(ts.findNumberOfShipsInRangeByCriteria(any())).willAnswer(invocation -> {
 *         ShipSearchCriteria criteria = (ShipSearchCriteria) invocation.getArguments()[0];
 *         return criteria.getMinimumRange() > 1000 ? 4 : 0;
 *     });
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(1500, 2))).isEqualTo(4);
 *     //expect
 *     assertThat(ts.findNumberOfShipsInRangeByCriteria(new ShipSearchCriteria(700, 2))).isEqualTo(0);
 * }
 * </code></pre>
 *
 * Backported from https://github.com/szpak/mockito-java8/
 *
 * @param <T> type of argument
 *
 * @author Marcin Zajączkowski
 * @since 3.0.0
 */
@Incubating
public class LambdaMatcher<T> implements ArgumentMatcher<T> {

    private final Predicate<T> predicate;
    private final String description;

    public LambdaMatcher(Predicate<T> predicate, String description) {
        this.predicate = predicate;
        this.description = description;
    }

    @Override
    public boolean matches(T item) {
        try {
            return predicate.test((T)item);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
