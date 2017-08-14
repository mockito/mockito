/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListOfSizeTest {

    @Test
    public void emptyList() throws Exception {
        ArrayList<Object> list = createListWithObjects();
        ListOfSize listOfSize = new ListOfSize(0);

        assertThat(listOfSize.matches(list)).isTrue();
    }

    @Test
    public void listWithSingleObject() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object());
        ListOfSize listOfSize = new ListOfSize(1);

        assertThat(listOfSize.matches(list)).isTrue();
    }

    @Test
    public void errorMessageContainsSizes() throws Exception {
        ArrayList<Object> list = createListWithObjects(new Object());
        ListOfSize listOfSize = new ListOfSize(0);

        listOfSize.matches(list);

        assertThat(listOfSize.toString()).contains("1", "0");
    }

    private ArrayList<Object> createListWithObjects(Object... objects) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(objects));
        return list;
    }
}
