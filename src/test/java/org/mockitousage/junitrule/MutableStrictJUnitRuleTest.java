/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

import static org.mockito.Mockito.when;

public class MutableStrictJUnitRuleTest {

    JUnitCore runner = new JUnitCore();

    @Test public void rule_can_be_changed_to_strict() {
        //when
        Result result = runner.run(LenientByDefault.class);

        //then
        JUnitResultAssert.assertThat(result)
                .succeeds(1)
                .fails(1, RuntimeException.class);
    }

    @Test public void rule_can_be_changed_to_lenient() {
        //when
        Result result = runner.run(StrictByDefault.class);

        //then
        JUnitResultAssert.assertThat(result)
                .succeeds(1)
                .fails(1, RuntimeException.class);
    }

    public static class LenientByDefault {
        @Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT);
        @Mock IMethods mock;

        @Test public void unused_stub() {
            when(mock.simpleMethod()).thenReturn("1");
        }

        @Test public void unused_stub_with_strictness() {
            //making Mockito strict only for this test method
            mockito.strictness(Strictness.STRICT_STUBS);

            when(mock.simpleMethod()).thenReturn("1");
        }
    }

    public static class StrictByDefault {
        @Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
        @Mock IMethods mock;

        @Test public void unused_stub() {
            when(mock.simpleMethod()).thenReturn("1");
        }

        @Test public void unused_stub_with_lenient() {
            //making Mockito lenient only for this test method
            mockito.strictness(Strictness.LENIENT);

            when(mock.simpleMethod()).thenReturn("1");
        }
    }
}
