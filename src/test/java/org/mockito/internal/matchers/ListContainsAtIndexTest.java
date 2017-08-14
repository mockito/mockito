/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListContainsAtIndexTest {

    private Object expectedObject = new Object();

    @Test
    public void listContainsValueAtIndex() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object(), expectedObject);
        ListContainsAtIndex<Object> containsAtIndex = new ListContainsAtIndex<Object>(expectedObject, 1);

        assertThat(containsAtIndex.matches(list)).isTrue();
    }

    @Test
    public void listDoesntContainValueAtIndex() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object(), new Object());
        ListContainsAtIndex<Object> containsAtIndex = new ListContainsAtIndex<Object>(expectedObject, 1);

        assertThat(containsAtIndex.matches(list)).isFalse();
    }

    @Test
    public void errorMessageContainsObject() throws Exception {
        ListContainsAtIndex<Object> containsAtIndex = new ListContainsAtIndex<Object>(expectedObject, 1);

        assertThat(containsAtIndex.toString()).contains(expectedObject.toString());
    }

    @Test
    public void errorMessageContainsIndex() throws Exception {
        ListContainsAtIndex<Object> containsAtIndex = new ListContainsAtIndex<Object>(expectedObject, 1);

        assertThat(containsAtIndex.toString()).contains("1");
    }

    private ArrayList<Object> createListWithObjects(Object... objects) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(objects));
        return list;
    }
}
