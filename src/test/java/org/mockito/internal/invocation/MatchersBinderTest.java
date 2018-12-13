/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitoutil.TestBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.internal.matchers.MatcherMarkers.markerOf;

public class MatchersBinderTest extends TestBase {

    private static final Location LOCATION = new LocationImpl();

    private static final Integer NON_MARKER_PRIMITIVE = markerOf(int.class) + 1;
    private static final String NON_MARKER_STRING = markerOf(String.class) + "x";
    private static final Object NON_MARKER_OBJECT = new Object();
    private static final Collection<?> NON_MARKER_COLLECTION = Arrays.asList(1, 2);
    private static final Object[] NON_MARKER_ARRAY = new Object[]{3, 4};

    private MatchersBinder binder;

    @Before
    public void setup() {
        binder = new MatchersBinder();
    }

    @Test
    public void no_matchers_no_arguments() {
        List<LocalizedMatcher> matchers = createMatchers();
        Invocation invocation = createInvocation();

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation);
    }

    @Test
    public void no_matchers_with_arguments_no_markers() {
        List<LocalizedMatcher> matchers = createMatchers();
        Invocation invocation = createInvocation(NON_MARKER_PRIMITIVE, NON_MARKER_STRING, NON_MARKER_OBJECT,
            NON_MARKER_COLLECTION, NON_MARKER_ARRAY);

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation, new Equals(NON_MARKER_PRIMITIVE), new Equals(NON_MARKER_STRING),
            new Equals(NON_MARKER_OBJECT), new Equals(NON_MARKER_COLLECTION), new ArrayEquals(NON_MARKER_ARRAY));
    }

    @Test
    public void no_matchers_with_arguments_some_markers() {
        List<LocalizedMatcher> matchers = createMatchers();
        Invocation invocation = createInvocation(NON_MARKER_PRIMITIVE, markerOf(String.class), markerOf(Object.class),
            markerOf(List.class), NON_MARKER_ARRAY);

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation, new Equals(NON_MARKER_PRIMITIVE), new Equals(markerOf(String.class)),
            new Equals(markerOf(Object.class)), new Equals(markerOf(List.class)), new ArrayEquals(NON_MARKER_ARRAY));
    }

    @Test
    public void no_matchers_with_arguments_only_markers() {
        List<LocalizedMatcher> matchers = createMatchers();
        Invocation invocation = createInvocation(markerOf(int.class), markerOf(String.class), markerOf(Object.class),
            markerOf(List.class), markerOf(Object[].class));

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation, new Equals(markerOf(int.class)), new Equals(markerOf(String.class)),
            new Equals(markerOf(Object.class)), new Equals(markerOf(List.class)), new Equals(markerOf(Object[].class)));
    }

    @Test
    public void with_matchers_no_arguments() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1), new Equals("2"));
        Invocation invocation = createInvocation();

        try {
            binder.bindMatchers(matchers, invocation);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertException(e, 0, 2);
        }
    }

    @Test
    public void with_matchers_with_arguments_no_markers() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1), new Equals("2"));
        Invocation invocation = createInvocation(NON_MARKER_PRIMITIVE, NON_MARKER_STRING);

        try {
            binder.bindMatchers(matchers, invocation);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertException(e, 0, 2);
        }
    }

    @Test
    public void with_matchers_with_arguments_same_amount_of_markers_and_matchers() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1), new Equals("2"));
        Invocation invocation = createInvocation(markerOf(int.class), NON_MARKER_OBJECT, markerOf(String.class),
            NON_MARKER_ARRAY);

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation, new Equals(1), new Equals(NON_MARKER_OBJECT), new Equals("2"),
            new ArrayEquals(NON_MARKER_ARRAY));
    }

    @Test
    public void with_matchers_with_arguments_only_markers_same_argument_type() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1), new Equals(2));
        Invocation invocation = createInvocation(markerOf(int.class), markerOf(int.class));

        InvocationMatcher matcher = binder.bindMatchers(matchers, invocation);

        assertMatcher(matcher, invocation, new Equals(1), new Equals(2));
    }

    @Test
    public void with_matchers_with_arguments_more_matchers_than_markers() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1), new Equals(2));
        Invocation invocation = createInvocation(markerOf(int.class), NON_MARKER_PRIMITIVE);

        try {
            binder.bindMatchers(matchers, invocation);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertException(e, 1, 2);
        }
    }

    @Test
    public void with_matchers_with_arguments_more_markers_than_matchers() {
        List<LocalizedMatcher> matchers = createMatchers(new Equals(1));
        Invocation invocation = createInvocation(markerOf(int.class), markerOf(int.class), NON_MARKER_PRIMITIVE);

        try {
            binder.bindMatchers(matchers, invocation);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertException(e, 2, 1);
        }
    }

    private Invocation createInvocation(final Object... arguments) {

        MockitoMethod mockitoMethod = new MockitoMethod() {

            @Override
            public boolean isAbstract() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Class<?> getReturnType() {
                return null;
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[arguments.length];
            }

            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[0];
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public Method getJavaMethod() {
                return null;
            }
        };

        return new InterceptedInvocation(null, mockitoMethod, arguments, null, LOCATION, 0);
    }

    private List<LocalizedMatcher> createMatchers(ArgumentMatcher<?>... matchers) {

        List<LocalizedMatcher> localizedMatchers = new ArrayList<LocalizedMatcher>(matchers.length);
        for (ArgumentMatcher<?> matcher : matchers) {
            localizedMatchers.add(new LocalizedMatcher(matcher, LOCATION));
        }
        return localizedMatchers;
    }

    private void assertMatcher(InvocationMatcher matcher, Invocation invocation, ArgumentMatcher<?>... expectedMatchers) {

        assertSame(invocation, matcher.getInvocation());
        assertEquals(Arrays.asList(expectedMatchers), matcher.getMatchers());
    }


    private void assertException(InvalidUseOfMatchersException exception, int expectedMatchers, int actualMatchers) {

        String exceptionMessage = exception.getMessage();

        assertThat(exceptionMessage)
            .contains(expectedMatchers + " matchers expected")
            .contains(actualMatchers + " recorded");

        assertThat(exceptionMessage).contains(LOCATION.toString());
    }
}
