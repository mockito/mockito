package org.mockito.hamcrest;

import org.hamcrest.Matcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * TODO: javadoc
 */
public class MockitoHamcrest {

    private static final MockingProgress MOCKING_PROGRESS = new ThreadSafeMockingProgress();

    /**
     * Allows matching arguments with hamcrest matchers.
     * <p>
     * In rare cases when the parameter is a primitive then you <b>*must*</b> use relevant intThat(), floatThat(), etc. method.
     * This way you will avoid <code>NullPointerException</code> during auto-unboxing.
     * <p>
     * See examples in javadoc for {@link MockitoHamcrest} class
     *
     * @param matcher decides whether argument matches
     *
     * @return <code>null</code>.
     */
    public static <T> T argThat(Matcher<T> matcher) {
        return (T) MOCKING_PROGRESS.getArgumentMatcherStorage()
                .reportMatcher(new HamcrestArgumentMatcher(matcher))
                .returnNull();
    }

    /*
    private static Class grabClass(Matcher matcher) {
        if (matcher.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            Type[] genericTypes = ((ParameterizedType) matcher.getClass().getGenericSuperclass()).getActualTypeArguments();
            if (genericTypes.length > 0) {
                return  (Class) genericTypes[0];
            }
        }
        return Object.class;
    }
    */
}
