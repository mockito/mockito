/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.collections;

import static java.util.Arrays.asList;
import static org.mockitoutil.ExtraMatchers.hasExactlyInOrder;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ListUtilTest extends TestBase {

    @Test
    public void shouldFilterList() throws Exception {
        final List list = asList("one", "x", "two", "x", "three");
        final List filtered = ListUtil.filter(list, new Filter() {
            public boolean isOut(final Object object) {
                return object == "x";
            }
        });
        
        assertThat(filtered, hasExactlyInOrder("one", "two", "three"));
    }
    
    @Test
    public void shouldReturnEmptyIfEmptyListGiven() throws Exception {
        final List list = new LinkedList();
        final List filtered = ListUtil.filter(list, null);
        assertTrue(filtered.isEmpty());
    }
}
