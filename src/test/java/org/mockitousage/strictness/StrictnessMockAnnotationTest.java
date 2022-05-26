/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.when;

public class StrictnessMockAnnotationTest {

    public @Rule MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock(strictness = Strictness.LENIENT)
    IMethods lenientMock;

    @Mock IMethods regularMock;

    @Test
    public void mock_is_lenient() {
        when(lenientMock.simpleMethod("1")).thenReturn("1");

        // then lenient mock does not throw:
        ProductionCode.simpleMethod(lenientMock, "3");
    }

    @Test
    public void mock_is_strict() {
        when(regularMock.simpleMethod("2")).thenReturn("2");

        Assertions.assertThatThrownBy(() -> ProductionCode.simpleMethod(regularMock, "4"))
                .isInstanceOf(PotentialStubbingProblem.class);
    }
}
