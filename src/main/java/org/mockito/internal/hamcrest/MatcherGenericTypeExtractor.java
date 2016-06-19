package org.mockito.internal.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

import static org.mockito.internal.util.reflection.GenericTypeExtractor.genericTypeOf;

/**
 * Extracts generic type of matcher
 */
public class MatcherGenericTypeExtractor {

    /**
     * Gets the generic type of given matcher. For example,
     * for matcher class that extends BaseMatcher[Integer] this method returns Integer
     */
    public static Class<?> genericTypeOfMatcher(Class<?> matcherClass) {
        //TODO SF check if we can reuse it for Mockito ArgumentMatcher
        return genericTypeOf(matcherClass, BaseMatcher.class, Matcher.class);
    }
}