package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

import static org.mockito.internal.util.reflection.GenericTypeExtractor.genericTypeOf;

/**
 * Extracts generic type of matcher
 */
class MatcherGenericTypeExtractor {

    /**
     * Gets the generic type of given matcher. For example,
     * for matcher class that extends BaseMatcher[Integer] this method returns Integer
     */
    static Class genericTypeOfMatcher(Class matcherClass) {
        return genericTypeOf(matcherClass, BaseMatcher.class, Matcher.class);
    }
}