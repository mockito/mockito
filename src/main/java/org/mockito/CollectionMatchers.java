/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.internal.matchers.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

public class CollectionMatchers {

    public static <K> List listContains(K... object) {
        return argThat(new ListContains<K>(object));
    }

    public static <K> List listContains(K object, int index) {
        return argThat(new ListContainsAtIndex<K>(object, index));
    }

    public static List listContainsNull() {
        return argThat(new ListContainsNull());
    }

    public static <K> List listDoesNotContain(K... object) {
        return argThat(new ListDoesNotContain<K>(object));
    }

    public static <K> List listDoesNotContain(K object, int index) {
        return argThat(new ListDoesNotContainAtIndex<K>(object, index));
    }

    public static List listDoesnotContainNull() {
        return argThat(new ListDoesNotContainNull());
    }

    public static List listOfSize(int size) {
        return argThat(new ListOfSize(size));
    }
}
