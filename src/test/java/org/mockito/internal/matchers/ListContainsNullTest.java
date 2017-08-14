/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListContainsNullTest {

    @Test
    public void listContainsNull() throws Exception {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(null);

        assertThat(new ListContainsNull().matches(list)).isTrue();
    }

    @Test
    public void listDoesNotContainNull() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object());

        assertThat(new ListContainsNull().matches(list)).isFalse();
    }

    @Test
    public void errorMessageContainsNull() throws Exception {
        assertThat(new ListContainsNull().toString()).contains("null");
    }

    private ArrayList<Object> createListWithObjects(Object... objects) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(objects));
        return list;
    }
}
