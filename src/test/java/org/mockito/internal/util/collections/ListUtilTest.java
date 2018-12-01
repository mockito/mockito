/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.collections;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil.Converter;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.internal.util.collections.ListUtil.Predicate;
import org.mockitoutil.TestBase;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ListUtilTest extends TestBase {

    private static class TestFilter implements Filter<String> {
        private final String match;

        private TestFilter(String match) {
            assert match != null;
            this.match = match;
        }


        @Override
        public boolean isOut(String object) {
            return match.equals(String.valueOf(object));
        }
    }

    private static class TestPredicate extends Predicate<String> {
        private final String match;

        private TestPredicate(String match) {
            assert match != null;
            this.match = match;
        }

        @Override
        public boolean test(String object) {
            return match.equals(String.valueOf(object));
        }
    }

    @Test
    public void shouldFilterList() {
        List<String> list = asList("one", "x", "two", "x", "three");
        List<String> filtered = ListUtil.filter(list, new TestFilter("x"));

        assertThat(filtered).containsSequence("one", "two", "three");
    }

    @Test
    public void filterShouldReturnEmptyIfEmptyListGiven() {
        List<Object> list = new LinkedList<Object>();
        List<Object> filtered = ListUtil.filter(list, null);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void shouldFilterByList() {
        List<String> list = asList("one", "x", "two", "x", "three");
        List<String> filtered = ListUtil.filterBy(list, new TestPredicate("x"));

        assertThat(filtered).containsExactly("x", "x");
    }

    @Test
    public void filterByShouldReturnEmptyIfEmptyListGiven() {
        List<Object> list = new LinkedList<Object>();
        List<Object> filtered = ListUtil.filterBy(list, null);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void convertShouldConvertAllElements() {
        List<Integer> input = asList(1, 2, 3, 4);
        LinkedList<String> converted = ListUtil.convert(input, new Converter<Integer, String>() {

            @Override
            public String convert(Integer integer) {
                return String.valueOf(integer);
            }
        });

        assertThat(converted).containsAll(asList("1", "2", "3", "4"));
    }
}
