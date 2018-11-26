/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitousage.strictness.ProductionCode;
import org.mockitoutil.JUnitResultAssert;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.when;

public class StrictStubsRunnerTest extends TestBase {

    JUnitCore runner = new JUnitCore();

    @Test public void detects_unnecessary_stubbings() {
        //when
        Result result = runner.run(UnnecessaryStubbing.class);
        //then
        JUnitResultAssert.assertThat(result)
                .fails(1, UnnecessaryStubbingException.class)
                .succeeds(2);
    }

    @Test public void fails_fast_on_argument_mismatch() {
        //when
        Result result = runner.run(StubbingArgMismatch.class);
        //then
        JUnitResultAssert.assertThat(result)
                .succeeds(2)
                .fails(1, PotentialStubbingProblem.class);
    }

    @RunWith(MockitoJUnitRunner.StrictStubs.class)
    public static class UnnecessaryStubbing {
        @Mock IMethods mock;
        @Test public void unused_stubbing_1() {
            when(mock.simpleMethod()).thenReturn("");
        }
        @Test public void unused_stubbing_2() {
            when(mock.simpleMethod()).thenReturn("");
        }
        @Test public void correct_stubbing() {
            when(mock.simpleMethod()).thenReturn("");
            mock.simpleMethod();
        }
    }

    @RunWith(MockitoJUnitRunner.StrictStubs.class)
    public static class StubbingArgMismatch {
        @Mock IMethods mock;
        @Test public void passing1() {}
        @Test public void passing2() {
            when(mock.simpleMethod()).thenReturn("");
            mock.simpleMethod();
        }
        @Test public void argument_mismatch() {
            when(mock.simpleMethod(10)).thenReturn("");
            ProductionCode.simpleMethod(mock, 20);
        }
    }
}
