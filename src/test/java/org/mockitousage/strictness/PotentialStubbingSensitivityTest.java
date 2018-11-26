/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.strictness;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.when;

public class PotentialStubbingSensitivityTest {

    @Rule public MockitoRule mockito = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
    @Mock IMethods mock;

    @Before
    public void setup() {
        when(mock.simpleMethod("1")).thenReturn("1");
    }

    @Test
    public void allows_stubbing_with_different_arg_in_test_code() {
        //although we are calling 'simpleMethod' with different argument
        //Mockito understands that this is stubbing in the test code and does not trigger PotentialStubbingProblem
        when(mock.simpleMethod("2")).thenReturn("2");

        //methods in anonymous inner classes are ok, too
        new Runnable() {
            public void run() {
                when(mock.simpleMethod("3")).thenReturn("3");
            }
        }.run();

        //avoiding unnecessary stubbing failures:
        mock.simpleMethod("1");
        mock.simpleMethod("2");
        mock.simpleMethod("3");
    }

    @Test
    public void reports_potential_stubbing_problem_in_production_code() {
        //when
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() throws Throwable {
                ProductionCode.simpleMethod(mock, "2");
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }
}
