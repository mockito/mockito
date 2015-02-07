package org.mockitousage.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockitousage.IMethods;

import java.util.Arrays;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DescribeMismatchTest {

    private class MatchesNothing extends BaseMatcher<Object> {
        public boolean matches(Object o) {
            return false;
        }
        public void describeTo(Description description) {
            description.appendText("nothing");
        }
    }

    private class MatchesNothingDescribesMismatch extends MatchesNothing {
        public void describeMismatch(Object item, Description mismatchDescription) {
            mismatchDescription.appendText("my description of " + item);
        }
    }

    private class MatchesNothingVarargs extends BaseMatcher<String[]> implements VarargMatcher {
        public boolean matches(Object o) {
            return false;
        }
        public void describeTo(Description description) {
            description.appendText("nothing");
        }
        public void describeMismatch(Object item, Description mismatchDescription) {
            mismatchDescription.appendText("my description of " + item);
        }
    }

    public IMethods mock = mock(IMethods.class);

    @Test
    public void uses_mismatch_description_in_matcher_if_available() {
        mock.oneArg(3);
        try {
            verify(mock).oneArg(argThat(new MatchesNothingDescribesMismatch()));
            fail("this can't be - matcher should always fail");
        } catch (ArgumentsAreDifferent aad) {
            assertThat(aad.getMessage()).contains("oneArg(my description of 3)");
            System.out.println(aad.getMessage());
        }
    }

    @Test
    public void use_default_description_if_mismatch_description_is_not_available() {
        mock.oneArg(3);
        try {
            verify(mock).oneArg(argThat(new MatchesNothing()));
            fail("this can't be - matcher should always fail");
        } catch (ArgumentsAreDifferent aad) {
            assertThat(aad.getMessage()).contains("oneArg(3)");
            System.out.println(aad.getMessage());
        }
    }

    @Test
    public void use_default_description_if_no_matchers_are_given() {
        mock.oneArg(3);
        try {
            verify(mock).oneArg(5);
        } catch (ArgumentsAreDifferent aad) {
            assertThat(aad.getMessage()).contains("oneArg(3)");
            System.out.println(aad.getMessage());
        }
    }

    @Test
    public void some_matchers_describe_mismatch_some_dont() {
        mock.threeArgumentMethod(1, 2, "3");
        try {
            verify(mock).threeArgumentMethod(eq(1), argThat(new MatchesNothingDescribesMismatch()), eq("3"));//.simpleMethod(argThat(new MatchesNothingDescribesMismatch()), argThat(new MatchesNothingDescribesMismatch()));
        } catch (ArgumentsAreDifferent aad) {
            assertThat(aad.getMessage()).contains("1,");
            assertThat(aad.getMessage()).contains("my description of 2,");
            assertThat(aad.getMessage()).contains("\"3\"");
            System.out.println(aad.getMessage());
        }
    }

    @Test
    public void all_varargs_arguments_are_described_by_the_varargs_matcher() {
        mock.varargsString(5, "a", "b", "c");
        try {
            verify(mock).varargsString(eq(5), argThat(new MatchesNothingVarargs()));
            fail("this can't be - matcher should always fail");
        } catch (ArgumentsAreDifferent aad) {
            assertThat(aad.getMessage()).contains("my description of a");
            assertThat(aad.getMessage()).contains("my description of b");
            assertThat(aad.getMessage()).contains("my description of c");
        }
    }

}
