/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.internal.matchers.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * See {@link Matchers} for general info about matchers.
 * <p>
 * CollectionMatchers provides matchers that simplify verifications on standard java collections.
 * <p>
 *
 * Scroll down to see all methods - full list of matchers.
 *
 * @since 2.9.0
 */
public class CollectionMatchers {

    /**
     * list contains the given value
     * list contains all given values
     *
     * @since 2.9.0
     */
    public static <K> List listContains(K... object) {
        return argThat(new ListContains<K>(object));
    }

    /**
     * list contains the given value at the specified index
     *
     * @since 2.9.0
     */
    public static <K> List listContains(K object, int index) {
        return argThat(new ListContainsAtIndex<K>(object, index));
    }

    /**
     * list contains null
     *
     * @since 2.9.0
     */
    public static List listContainsNull() {
        return argThat(new ListContainsNull());
    }

    /**
     * list does not contain the given value
     * list does not contain all given values
     *
     * @since 2.9.0
     */
    public static <K> List listDoesNotContain(K... object) {
        return argThat(new ListDoesNotContain<K>(object));
    }

    /**
     * list does not contain the given value at the specified index
     *
     * @since 2.9.0
     */
    public static <K> List listDoesNotContain(K object, int index) {
        return argThat(new ListDoesNotContainAtIndex<K>(object, index));
    }

    /**
     * list does not contain null
     *
     * @since 2.9.0
     */
    public static List listDoesnotContainNull() {
        return argThat(new ListDoesNotContainNull());
    }

    /**
     * list has exactly the specified size
     *
     * @since 2.9.0
     */
    public static List listOfSize(int size) {
        return argThat(new ListOfSize(size));
    }
}
