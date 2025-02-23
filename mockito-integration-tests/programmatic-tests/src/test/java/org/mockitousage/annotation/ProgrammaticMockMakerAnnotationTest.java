/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockMakers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ProgrammaticMockMakerAnnotationTest {
    @Mock(mockMaker = MockMakers.INLINE)
    ClassWithFinalMethod inlineMock;

    @Mock(mockMaker = MockMakers.SUBCLASS)
    ClassWithFinalMethod subclassMock;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_mock_uses_given_mock_maker() {
        Mockito.when(inlineMock.finalMethodCallingNonFinal()).thenReturn("MOCKED");
        Mockito.when(subclassMock.finalMethodCallingNonFinal()).thenReturn("MOCKED");

        assertEquals("MOCKED", inlineMock.finalMethodCallingNonFinal());
        assertEquals("ORIGINAL", subclassMock.finalMethodCallingNonFinal());
        assertEquals("MOCKED", subclassMock.nonFinal());
    }

    private static class ClassWithFinalMethod {
        final String finalMethodCallingNonFinal() {
            nonFinal();
            return "ORIGINAL";
        }

        String nonFinal() {
            return "ORIGINAL";
        }
    }
}
