/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.collections;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertTrue;

public class ListUtilTest extends TestBase {

    @Test
    public void shouldFilterList() throws Exception {
        List<String> list = asList("one", "x", "two", "x", "three");
        List<String> filtered = ListUtil.filter(list, new Filter<String>() {
            public boolean isOut(String object) {
                return object == "x";
            }
        });

        Assertions.assertThat(filtered).containsSequence("one", "two", "three");
    }
    
    @Test
    public void shouldReturnEmptyIfEmptyListGiven() throws Exception {
        List<Object> list = new LinkedList<Object>();
        List<Object> filtered = ListUtil.filter(list, null);
        assertTrue(filtered.isEmpty());
    }
}
