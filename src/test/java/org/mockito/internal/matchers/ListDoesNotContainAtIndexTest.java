/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListDoesNotContainAtIndexTest {

    private Object expectedObject = new Object();

    @Test
    public void listDoenstContainValueAtIndex() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object(), new Object());
        ListDoesNotContainAtIndex<Object> doesNotContainAtIndex = new ListDoesNotContainAtIndex<Object>(expectedObject, 1);

        assertThat(doesNotContainAtIndex.matches(list)).isTrue();
    }

    @Test
    public void listContainsValueAtIndex() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object(), expectedObject);
        ListDoesNotContainAtIndex<Object> doesNotContainAtIndex = new ListDoesNotContainAtIndex<Object>(expectedObject, 1);

        assertThat(doesNotContainAtIndex.matches(list)).isFalse();
    }


    @Test
    public void errorMessageContainsObject() throws Exception {
        ListDoesNotContainAtIndex<Object> doesNotContainAtIndex = new ListDoesNotContainAtIndex<Object>(expectedObject, 1);

        assertThat(doesNotContainAtIndex.toString()).contains(expectedObject.toString());
    }

    @Test
    public void errorMessageContainsIndex() throws Exception {
        ListDoesNotContainAtIndex<Object> doesNotContainAtIndex = new ListDoesNotContainAtIndex<Object>(expectedObject, 1);

        assertThat(doesNotContainAtIndex.toString()).contains("1");
    }

    private ArrayList<Object> createListWithObjects(Object... objects) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(objects));
        return list;
    }
}
