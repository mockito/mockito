package org.mockito.internal.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.isA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CapturingMatcherParameterizedTest {

    private boolean actual, expected;

    public CapturingMatcherParameterizedTest(boolean actual, boolean expected) {
        this.actual = actual;
        this.expected = expected;
    }

    @Test
    public void verify_scenario() {
        assertThat(expected, is(equalTo(actual)));
    }

    @Parameters
    public static List<Object[]> scenarios() {
        return Arrays.asList(
            getParams(new CapturingMatcher(isA(String.class), new ArrayList<String>()).matches("string_type"), true),
            getParams(new CapturingMatcher(isA(String.class), new ArrayList<String>()).matches(1), false),
            getParams(new CapturingMatcher(isA(List.class), new ArrayList<List>()).matches(new ArrayList<String>()), true),
            getParams(new CapturingMatcher(isA(List.class), new ArrayList<List>()).matches("different_type"), false),
            getParams(new CapturingMatcher(isA(ArrayList.class), new ArrayList<ArrayList>()).matches(new ArrayList<String>()), true),
            getParams(new CapturingMatcher(isA(Object.class), new ArrayList<Object>()).matches(new ArrayList<String>()), true)
        );
    }

    private static Object[] getParams(boolean actual, boolean expected) {
        return new Object[] { actual, expected };
    }

}
