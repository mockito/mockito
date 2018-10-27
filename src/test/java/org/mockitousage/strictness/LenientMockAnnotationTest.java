/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.strictness;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.when;

public class LenientMockAnnotationTest {

    public @Rule MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    @Mock(lenient = true) IMethods lenientMock;
    @Mock IMethods regularMock;

    @Test
    public void mock_is_lenient() {
        when(lenientMock.simpleMethod("1")).thenReturn("1");
        when(regularMock.simpleMethod("2")).thenReturn("2");

        //then lenient mock does not throw:
        ProductionCode.simpleMethod(lenientMock, "3");

        //but regular mock throws:
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                ProductionCode.simpleMethod(regularMock,"4");
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }
}
