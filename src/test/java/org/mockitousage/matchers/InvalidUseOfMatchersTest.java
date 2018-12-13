/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.MatcherMarkers;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InvalidUseOfMatchersTest {

    private IMethods mock = Mockito.mock(IMethods.class);

    @Test
    public void should_detect_stupid_use_of_matchers_when_verifying() {
        mock.oneArg(true);
        eq("that's the stupid way");
        eq("of using matchers");
        try {
            Mockito.verify(mock).oneArg(true);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("Misplaced or misused argument matcher detected here");
            e.printStackTrace();
        }
    }

    @Test
    public void should_not_scream_on_correct_usage() throws Exception {
        mock.simpleMethod(AdditionalMatchers.not(eq("asd")));
        mock.simpleMethod(AdditionalMatchers.or(eq("jkl"), eq("asd")));
    }

    @Test
    public void should_scream_when_no_matchers_inside_not() {
        try {
            mock.simpleMethod(AdditionalMatchers.not("jkl"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("No matchers found for")
                .containsIgnoringCase("Not(?)");
        }
    }

    @Test
    public void should_scream_when_not_enough_matchers_inside_or_AddtionalMatcher() {
        try {
            mock.simpleMethod(AdditionalMatchers.or(eq("jkl"), "asd"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .containsIgnoringCase("inside additional matcher Or(?)")
                .contains("2 sub matchers expected")
                .contains("1 recorded");
        }
    }

    @Test
    public void should_scream_when_matchers_mixed_with_markers() {

        try {
            mock.twoArgumentMethod(eq(1), MatcherMarkers.markerOf(int.class));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("2 matchers expected")
                .contains("1 recorded");
        }

        try {
            mock.threeArgumentMethodWithStrings(eq(1), eq("2"), MatcherMarkers.markerOf(String.class));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("3 matchers expected")
                .contains("2 recorded");
        }

        try {
            mock.threeArgumentMethod(eq(1), MatcherMarkers.markerOf(this), eq("3"));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("3 matchers expected")
                .contains("2 recorded");
        }
    }

    @Test
    public void should_mention_matcher_when_misuse_detected() {
        // Given


        // When
        Result run = new JUnitCore().run(ObjectMatcherMisuseOnPrimitiveSite.class);

        // Then

        assertThat(run.getFailures()).hasSize(2);
        assertThat(run.getFailures().get(0).getException()).isInstanceOf(NullPointerException.class)
            .hasMessage(null);
        assertThat(run.getFailures().get(1).getException()).isInstanceOf(InvalidUseOfMatchersException.class)
            .hasMessageContaining("primitive alternatives");
        new StateMaster().reset();

    }

    @Test
    public void should_scream_when_matcher_type_casted() {

        try {
            int expected = 10;
            mock.longArg(eq(expected));
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e.getMessage())
                .contains("0 matchers expected")
                .contains("1 recorded");
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class ObjectMatcherMisuseOnPrimitiveSite {
        @Test
        public void fails_with_NPE() {
            IMethods mock = Mockito.mock(IMethods.class);
            doNothing().when(mock)
                .twoArgumentMethod(eq(73),
                    (Integer) any()); // <= Raise NPE on this call site
        }

    }
}
