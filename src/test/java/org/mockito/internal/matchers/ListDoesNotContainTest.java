/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListDoesNotContainTest {

    private Object expectedObject = new Object();
    private Object expectedObject2 = new Object();

    @Test
    public void listWithSingleObject() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object());

        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject);

        assertThat(listDoesNotContain.matches(list)).isTrue();
    }

    @Test
    public void listContainsObject() throws Exception {
        ArrayList<Object> list = createListWithObjects(expectedObject);

        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject);

        assertThat(listDoesNotContain.matches(list)).isFalse();
    }

    @Test
    public void listWithMultipleObjects() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object(), expectedObject);

        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject);

        assertThat(listDoesNotContain.matches(list)).isFalse();
    }

    @Test
    public void listDoesNotContainMultipleObjects() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object());

        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject,expectedObject2);

        assertThat(listDoesNotContain.matches(list)).isTrue();
    }

    @Test
    public void listContainsOneOfTheObjects() throws Exception {
        ArrayList<Object> list = createListWithObjects(expectedObject, new Object());

        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject,expectedObject2);

        assertThat(listDoesNotContain.matches(list)).isFalse();
    }

    @Test
    public void errorMessageContainsObject() throws Exception {
        ListDoesNotContain<Object> listDoesNotContain = new ListDoesNotContain<Object>(expectedObject);

        assertThat(listDoesNotContain.toString()).contains(expectedObject.toString());
    }

    private ArrayList<Object> createListWithObjects(Object... objects) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(objects));
        return list;
    }
}
