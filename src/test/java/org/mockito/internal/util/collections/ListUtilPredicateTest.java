/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.collections;

import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ListUtilPredicateTest {

    static class SubstringPredicate extends Predicate<String> {
        private final String substring;

        public SubstringPredicate(String substring) {
            this.substring = substring;
        }

        @Override
        public boolean test(String object) {
            return object.contains(this.substring);
        }
    }

    private final SubstringPredicate fooPredicate = new SubstringPredicate("foo");
    private final SubstringPredicate barPredicate = new SubstringPredicate("bar");

    @Test
    public void testPredicate() {
        assertThat(fooPredicate.test("foo")).isTrue();
        assertThat(fooPredicate.test("bar")).isFalse();
    }

    @Test
    public void testPredicateNegation() {
        assertThat(fooPredicate.negate().test("foo")).isFalse();
        assertThat(fooPredicate.negate().test("bar")).isTrue();
    }

    @Test
    public void testPredicateAnd() {
        assertThat(fooPredicate.and(barPredicate).test("foobar")).isTrue();
        assertThat(fooPredicate.and(barPredicate).test("foo")).isFalse();
        assertThat(fooPredicate.and(barPredicate).test("bar")).isFalse();
    }

    @Test
    public void testPredicateOr() {
        assertThat(fooPredicate.or(barPredicate).test("foo")).isTrue();
        assertThat(fooPredicate.or(barPredicate).test("bar")).isTrue();
        assertThat(fooPredicate.or(barPredicate).test("foobar")).isTrue();
        assertThat(fooPredicate.or(barPredicate).test("banana")).isFalse();
    }
}
