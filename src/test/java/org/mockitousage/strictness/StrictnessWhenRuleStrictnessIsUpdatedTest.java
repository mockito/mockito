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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class StrictnessWhenRuleStrictnessIsUpdatedTest {

    @Mock IMethods mock;
    @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);

    @Test
    public void strictness_per_mock() {
        //when
        rule.strictness(Strictness.STRICT_STUBS);

        //then previous mock is strict:
        when(mock.simpleMethod(1)).thenReturn("1");
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                ProductionCode.simpleMethod(mock, 2);
            }
        }).isInstanceOf(PotentialStubbingProblem.class);

        //but the new mock is lenient, even though the rule is not:
        final IMethods lenientMock = mock(IMethods.class, withSettings().lenient());
        when(lenientMock.simpleMethod(1)).thenReturn("1");
        lenientMock.simpleMethod(100);
    }

    @Test
    public void strictness_per_stubbing() {
        //when
        rule.strictness(Strictness.STRICT_STUBS);

        //then previous mock is strict:
        when(mock.simpleMethod(1)).thenReturn("1");
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                ProductionCode.simpleMethod(mock, 2);
            }
        }).isInstanceOf(PotentialStubbingProblem.class);

        //but the new mock is lenient, even though the rule is not:
        lenient().when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(100);
    }
}
