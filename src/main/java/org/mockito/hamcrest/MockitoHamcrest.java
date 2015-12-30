package org.mockito.hamcrest;

import org.hamcrest.Matcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.hamcrest.MatcherGenericTypeExtractor;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.ArgumentMatcher;

/**
 * Allows matching arguments with hamcrest matchers.
 * <b>Requires</b> <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a> on classpath,
 * Mockito <b>does not</b> depend on hamcrest!
 * Note the <b>NullPointerException</b> auto-unboxing caveat described below.
 * <p/>
 * Before implementing or reusing an existing hamcrest matcher please read
 * how to deal with sophisticated argument matching in {@link ArgumentMatcher}.
 * <p/>
 * Mockito 2.0 was decoupled from Hamcrest to avoid version incompatibilities
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
 *
 * @since 2.0
 */
public class MockitoHamcrest {

    private static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();

    /**
     * Allows matching arguments with hamcrest matchers.
     * <p/>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     * @return <code>null</code> or default value for primitive (0, false, etc.)
     * @since 2.0
     */
    public static <T> T argThat(Matcher<T> matcher) {
        return (T) reportMatcher(matcher)
                .returnFor(MatcherGenericTypeExtractor.genericTypeOfMatcher(matcher.getClass()));
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
        return reportMatcher(matcher).returnChar();
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
        return reportMatcher(matcher).returnFalse();
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
        return reportMatcher(matcher).returnZero();
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
        return reportMatcher(matcher).returnZero();
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
        return reportMatcher(matcher).returnZero();
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
        return reportMatcher(matcher).returnZero();
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
        return reportMatcher(matcher).returnZero();
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
        return reportMatcher(matcher).returnZero();
    }

    private static <T> HandyReturnValues reportMatcher(Matcher<T> matcher) {
        return MOCKING_PROGRESS.getArgumentMatcherStorage()
                .reportMatcher(new HamcrestArgumentMatcher(matcher));
    }
}
