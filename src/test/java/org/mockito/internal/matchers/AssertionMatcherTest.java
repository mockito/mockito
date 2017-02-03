/*
 * Copyright (C) 2015 Mockito contributors.
 *
 * Licensed under the Apache License, Version 2.0.
 */
package org.mockito.internal.matchers;

import net.bytebuddy.ClassFileVersion;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.WithAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.matchers.domain.ShipSearchCriteria;
import org.mockito.internal.matchers.domain.TacticalStation;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static net.bytebuddy.ClassFileVersion.JAVA_V9;
import static org.junit.Assume.assumeTrue;
import static org.mockito.AdditionalMatchers.assertArg;
import static org.mockito.Mockito.verify;

public class AssertionMatcherTest implements WithAssertions {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private TacticalStation ts;

    private ShipSearchCriteria searchCriteria = new ShipSearchCriteria(1000, 4);

    @Test
    public void shouldAllowToUseArgumentCaptorInClassicWay() {
        //when
        ts.findNumberOfShipsInRangeByCriteria(searchCriteria);
        //then
        ArgumentCaptor<ShipSearchCriteria> captor = ArgumentCaptor.forClass(ShipSearchCriteria.class);
        verify(ts).findNumberOfShipsInRangeByCriteria(captor.capture());
        assertThat(captor.getValue().getMinimumRange()).isLessThan(2000);
    }

    @Test
    public void shouldAllowToUseAssertionInLambda() {
        //given
        assumeTrue(ClassFileVersion.ofThisVm().isLessThan(JAVA_V9));
        //when
        ts.findNumberOfShipsInRangeByCriteria(searchCriteria);
        //then
        verify(ts).findNumberOfShipsInRangeByCriteria(assertArg(sc -> assertThat(sc.getMinimumRange()).isLessThan(2000)));
    }

    @Test
    public void shouldAllowToUseAssertionInLambdaWithPrimitiveAsArgument() {
        //given
        assumeTrue(ClassFileVersion.ofThisVm().isLessThan(JAVA_V9));
        //when
        ts.fireTorpedo(2);
        //then
        verify(ts).fireTorpedo(assertArg(i -> assertThat(i).isEqualTo(2)));
    }

    @Test
    public void shouldHaveMeaningfulErrorMessage() {
        //given
        assumeTrue(ClassFileVersion.ofThisVm().isLessThan(JAVA_V9));
        //when
        ts.findNumberOfShipsInRangeByCriteria(searchCriteria);
        //then
        ThrowableAssert.ThrowingCallable verifyLambda = () -> {
            verify(ts).findNumberOfShipsInRangeByCriteria(assertArg(sc -> assertThat(sc.getMinimumRange()).isLessThan(50)));
        };
        assertThatThrownBy(verifyLambda)
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Argument(s) are different! Wanted:\n" +
                        "ts.findNumberOfShipsInRangeByCriteria(\n" +
                        "    AssertionMatcher reported: \n" +
                        "Expecting:\n" +
                        " <1000>\n" +
                        "to be less than:\n" +
                        " <50> ")
                .hasMessageContaining("Actual invocation has different arguments:\n" +
                        "ts.findNumberOfShipsInRangeByCriteria(\n" +
                        "    ShipSearchCriteria{minimumRange=1000, numberOfPhasers=4}\n" +
                        ");");
    }
}
