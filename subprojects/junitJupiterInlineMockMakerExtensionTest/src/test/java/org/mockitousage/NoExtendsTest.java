/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.*;

class NoExtendsTest {

    @Mock
    private MockedStatic<Dummy> staticMethod;

    @Mock
    private MockedConstruction<Dummy> construction;

    @Test
    void runsStaticMethods() {
        assertThat(Dummy.foo()).isNull();
    }

    @Test
    void runsConstruction() {
        assertThat(new Dummy().bar()).isNull();
    }

    static class Dummy {

        static String foo() {
            return "foo";
        }

        String bar() {
            return "foo";
        }
    }
}
