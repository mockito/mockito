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
import org.mockitoutil.TestBase;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListUtilTest extends TestBase {

    private static class TestFilter implements Filter<String> {
        private final String match;

        private TestFilter(String match) {
            assert match != null;
            this.match = match;
        }

        @Override
        public String toString() {
            return "Filter[" + match + "]";
        }

        @Override
        public boolean isOut(String object) {
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
    public void shouldReturnEmptyIfEmptyListGiven() {
        List<Object> list = new LinkedList<Object>();
        List<Object> filtered = ListUtil.filter(list, null);
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void shouldBuildCorrectToStringForCombineOr() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("a"), new TestFilter("b"));

        assertThat(filter.toString()).isEqualTo("[Filter[a]||Filter[b]]");
    }

    @Test
    public void shouldBuildCorrectToStringForCombineOrWithFirstNull() {
        Filter<String> filter = ListUtil.combineOr(null, new TestFilter("b"));

        assertThat(filter.toString()).isEqualTo("[null||Filter[b]]");
    }

    @Test
    public void shouldBuildCorrectToStringForCombineOrWithSecondNull() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("a"), null);

        assertThat(filter.toString()).isEqualTo("[Filter[a]||null]");
    }

    @Test
    public void combineOrSShouldFilterOutOne() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("one"), new TestFilter("other"));

        assertTrue(filter.isOut("one"));
    }

    @Test
    public void combineOrShouldFilterOutOther() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("one"), new TestFilter("other"));

        assertTrue(filter.isOut("other"));
    }

    @Test
    public void combineOrAcceptsWanted() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("one"), new TestFilter("other"));

        assertFalse(filter.isOut("wanted"));
    }

    @Test
    public void combineOrShouldFilterAllOnFirstNull() {
        Filter<String> filter = ListUtil.combineOr(null, new TestFilter("other"));

        assertTrue(filter.isOut("wanted"));
    }

    @Test
    public void combineOrShouldFilterAllOnSecondNull() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("one"), null);

        assertTrue(filter.isOut("wanted"));
    }

    @Test
    public void combineOrShouldFilterAllOnBothNull() {
        Filter<String> filter = ListUtil.combineOr(new TestFilter("one"), null);

        assertTrue(filter.isOut("wanted"));
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
