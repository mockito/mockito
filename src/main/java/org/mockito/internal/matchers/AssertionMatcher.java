/*
 * Copyright (C) 2015 Mockito contributors.
 *
 * Licensed under the Apache License, Version 2.0.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.Incubating;

import java.util.function.Consumer;

/**
 * Allows creating inlined ArgumentCaptor with a lambda expression.
 * <p>
 * With Java 8 and lambda expressions ArgumentCaptor can be inlined:
 *
 * <pre class="code"><code class="java">
 *{@literal @}Test
 * public void shouldAllowToUseAssertionInLambda() {
 *   //when
 *   ts.findNumberOfShipsInRangeByCriteria(searchCriteria);
 *   //then
 *   verify(ts).findNumberOfShipsInRangeByCriteria(assertArg(sc -> assertThat(sc.getMinimumRange()).isLessThan(2000)));
 * }
 * </code></pre>
 *
 * in comparison to 3 lines in the classic way:
 *
 * <pre class="code"><code class="java">
 *{@literal @}Test
 * public void shouldAllowToUseArgumentCaptorInClassicWay() {  //old way
 *     //when
 *     ts.findNumberOfShipsInRangeByCriteria(searchCriteria);
 *     //then
 *     ArgumentCaptor<ShipSearchCriteria> captor = ArgumentCaptor.forClass(ShipSearchCriteria.class);
 *     verify(ts).findNumberOfShipsInRangeByCriteria(captor.capture());
 *     assertThat(captor.getValue().getMinimumRange()).isLessThan(2000);
 * }
 *
 * AssertJ assertions (<pre>assertThat()</pre> used in lambda generate meaningful error messages in face of failure, but any other assertion can be
 * used if needed/preffered.
 *
 * Backported from https://github.com/szpak/mockito-java8/
 *
 * @param <T> type of argument
 *
 * @author Marcin Zajączkowski
 * @since 3.0.0
 */
@Incubating
public class AssertionMatcher<T> implements ArgumentMatcher<T> {

    private final Consumer<T> assertingLambda;
    private String errorMessage;

    public AssertionMatcher(Consumer<T> assertingLambda) {
        this.assertingLambda = assertingLambda;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(Object argument) {
        try {
            assertingLambda.accept((T) argument);
            return true;
        } catch (AssertionError e) {
            errorMessage = e.getMessage();
            return false;
        }
    }

    @Override
    public String toString() {
        return "AssertionMatcher reported: " + errorMessage;
    }
}
