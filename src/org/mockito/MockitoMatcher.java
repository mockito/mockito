package org.mockito;

import org.mockito.internal.util.Decamelizer;

/**
 * Matcher of arguments, decoupled from hamcrest.
 */
public abstract class MockitoMatcher<T> {

    /**
     * Informs if this matcher accepts the given argument.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     *
     * @param argument
     *            the argument
     * @return true if this matcher accepts the given argument.
     */
    public abstract boolean matches(Object argument);

    /**
     * By default this method decamelizes class name to promote meaningful names for matcher classes.
     * <p>
     * For example <b>StringWithStrongLanguage</b> matcher will generate 'String with strong language' description in case of failure.
     * <p>
     * You might want to override this method to
     * provide more specific description of the matcher (useful when
     * verification failures are reported).
     */
    public String describe() {
        String className = getClass().getSimpleName();
        return Decamelizer.decamelizeMatcher(className);
    }
}
