/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.hamcrest;

import static org.mockito.internal.hamcrest.MatcherGenericTypeExtractor.genericTypeOfMatcher;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.mockito.internal.util.Primitives.defaultValue;

import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;

/**
 * Allows matching arguments with hamcrest matchers.
 * <b>Requires</b> <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a> on classpath,
 * Mockito <b>does not</b> depend on hamcrest!
 * Note the <b>NullPointerException</b> auto-unboxing caveat described below.
 * <p/>
 * Before implementing or reusing an existing hamcrest matcher please read
 * how to deal with sophisticated argument matching in {@link ArgumentMatcher}.
 * <p/>
 * Mockito 2.1.0 was decoupled from Hamcrest to avoid version incompatibilities
 * that have impacted our users in past. Mockito offers a dedicated API to match arguments
 * via {@link ArgumentMatcher}.
 * Hamcrest integration is provided so that users can take advantage of existing Hamcrest matchers.
 * <p/>
 * Example:
 * <pre>
 *     import static org.mockito.hamcrest.MockitoHamcrest.argThat;
 *
 *     //stubbing
 *     when(mock.giveMe(argThat(new MyHamcrestMatcher())));
 *
 *     //verification
 *     verify(mock).giveMe(argThat(new MyHamcrestMatcher()));
 * </pre>
 * <b>NullPointerException</b> auto-unboxing caveat.
 * In rare cases when matching primitive parameter types you <b>*must*</b> use relevant intThat(), floatThat(), etc. method.
 * This way you will avoid <code>NullPointerException</code> during auto-unboxing.
 * Due to how java works we don't really have a clean way of detecting this scenario and protecting the user from this problem.
 * Hopefully, the javadoc describes the problem and solution well.
 * If you have an idea how to fix the problem, let us know via the mailing list or the issue tracker.
 * <p/>
 * By default, a matcher passed to a varargs parameter will match against the first element in the varargs array.
 * To match against the raw varargs array pass the type of the varargs parameter to {@link MockitoHamcrest#argThat(Matcher, Class)}
 * <p/>
 * For example, to match any number of {@code String} values:
 * <pre>
 *     import static org.hamcrest.CoreMatchers.isA;
 *     import static org.mockito.hamcrest.MockitoHamcrest.argThat;
 *
 *     // Given:
 *     void varargMethod(String... args);
 *
 *     //stubbing
 *     when(mock.varargMethod(argThat(isA(String[].class), String[].class));
 *
 *     //verification
 *     verify(mock).giveMe(argThat(isA(String[].class), String[].class));
 * </pre>
 *
 * @since 2.1.0
 */
public final class MockitoHamcrest {

    /**
     * Allows matching arguments with hamcrest matchers.
     * <p/>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>null</code> or default value for primitive (0, false, etc.)
     * @since 2.1.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T argThat(Matcher<T> matcher) {
        reportMatcher(matcher);
        return (T) defaultValue(genericTypeOfMatcher(matcher.getClass()));
    }

    /**
     * Allows matching arguments with hamcrest matchers.
     * <p/>
     * This variant can be used to pass an explicit {@code type},
     * which can be useful to provide a matcher that matches against all
     * elements in a varargs parameter.
     * <p/>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @param type the type the matcher matches.
     * @return <code>null</code> or default value for primitive (0, false, etc.)
     * @since 5.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T argThat(Matcher<T> matcher, Class<T> type) {
        reportMatcher(matcher, type);
        return (T) defaultValue(genericTypeOfMatcher(matcher.getClass()));
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>char</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>char</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static char charThat(Matcher<Character> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>boolean</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>boolean</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>false</code>.
     */
    public static boolean booleanThat(Matcher<Boolean> matcher) {
        reportMatcher(matcher);
        return false;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>byte</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>byte</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static byte byteThat(Matcher<Byte> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>short</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>short</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static short shortThat(Matcher<Short> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>int</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>int</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static int intThat(Matcher<Integer> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>long</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>long</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static long longThat(Matcher<Long> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>float</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>float</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static float floatThat(Matcher<Float> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    /**
     * Enables integrating hamcrest matchers that match primitive <code>double</code> arguments.
     * Note that {@link #argThat} will not work with primitive <code>double</code> matchers due to <code>NullPointerException</code> auto-unboxing caveat.
     * <p/>
     * * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static double doubleThat(Matcher<Double> matcher) {
        reportMatcher(matcher);
        return 0;
    }

    private static <T> void reportMatcher(Matcher<T> matcher) {
        reportMatcher(new HamcrestArgumentMatcher<T>(matcher));
    }

    private static <T> void reportMatcher(Matcher<T> matcher, Class<T> type) {
        reportMatcher(new HamcrestArgumentMatcher<T>(matcher, type));
    }

    private static <T> void reportMatcher(final HamcrestArgumentMatcher<T> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
    }

    private MockitoHamcrest() {}
}
