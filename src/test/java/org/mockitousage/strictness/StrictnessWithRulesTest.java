/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.strictness;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

public class StrictnessWithRulesTest {

    @Mock IMethods mock;
    @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void potential_stubbing_problem() {
        //when
        when(mock.simpleMethod("1")).thenReturn("1");
        lenient().when(mock.differentMethod("2")).thenReturn("2");

        //then on lenient stubbing, we can call it with different argument:
        mock.differentMethod("200");

        //but on strict stubbing, we cannot:
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                ProductionCode.simpleMethod(mock, "100");
            }
        }).isInstanceOf(PotentialStubbingProblem.class);

        //let's use the strict stubbing so that it is not reported as failure by the rule:
        mock.simpleMethod("1");
    }

    @Test
    public void unnecessary_stubbing() {
        //this unnecessary stubbing is not flagged by the rule:
        lenient().when(mock.differentMethod("2")).thenReturn("2");
    }
}
