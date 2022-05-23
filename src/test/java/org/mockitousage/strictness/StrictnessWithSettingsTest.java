/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.*;

public class StrictnessWithSettingsTest {

    public @Rule MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    IMethods lenientMock;
    IMethods regularMock;
    IMethods strictMock;

    @Before
    public void before() {
        lenientMock = mock(IMethods.class, withSettings().strictness(Strictness.LENIENT));
        regularMock = mock(IMethods.class);
        strictMock = mock(IMethods.class, withSettings().strictness(Strictness.STRICT_STUBS));
    }

    @Test
    public void mock_is_lenient() {
        when(lenientMock.simpleMethod("1")).thenReturn("1");
        when(regularMock.simpleMethod("3")).thenReturn("3");
        when(strictMock.simpleMethod("2")).thenReturn("2");

        // then lenient mock does not throw:
        ProductionCode.simpleMethod(lenientMock, "3");

        // but regular mock throws:
        Assertions.assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            public void call() {
                                ProductionCode.simpleMethod(regularMock, "4");
                            }
                        })
                .isInstanceOf(PotentialStubbingProblem.class);

        // also strict mock throws:
        Assertions.assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            public void call() {
                                ProductionCode.simpleMethod(strictMock, "5");
                            }
                        })
                .isInstanceOf(PotentialStubbingProblem.class);
    }
}
