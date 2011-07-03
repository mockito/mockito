/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatchersToStringTest extends TestBase {

    @Test
    public void sameToStringWithString() {
        assertEquals("same(\"X\")", describe(new Same("X")));

    }

    @Test
    public void nullToString() {
        assertEquals("isNull()", describe(Null.NULL));
    }

    @Test
    public void notNullToString() {
        assertEquals("notNull()", describe(NotNull.NOT_NULL));
    }

    @Test
    public void anyToString() {
        assertEquals("<any>", describe(Any.ANY));
    }

    @Test
    public void sameToStringWithChar() {
        assertEquals("same('x')", describe(new Same('x')));
    }

    @Test
    public void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("same(X)", describe(new Same(o)));
    }

    @Test
    public void equalsToStringWithString() {
        assertEquals("\"X\"", describe(new Equals("X")));

    }

    @Test
    public void equalsToStringWithChar() {
        assertEquals("'x'", describe(new Equals('x')));
    }

    @Test
    public void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("X", describe(new Equals(o)));
    }

    @Test
    public void orToString() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        assertEquals("or(1, 2)", describe(new Or(matchers)));
    }

    @Test
    public void notToString() {
        assertEquals("not(1)", describe(new Not(new Equals(1))));
    }

    @Test
    public void andToString() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        assertEquals("and(1, 2)", describe(new And(matchers)));
    }

    @Test
    public void startsWithToString() {
        assertEquals("startsWith(\"AB\")", describe(new StartsWith("AB")));
    }

    @Test
    public void endsWithToString() {
        assertEquals("endsWith(\"AB\")", describe(new EndsWith("AB")));
    }

    @Test
    public void containsToString() {
        assertEquals("contains(\"AB\")", describe(new Contains("AB")));
    }

    @Test
    public void findToString() {
        assertEquals("find(\"\\\\s+\")", describe(new Find("\\s+")));
    }

    @Test
    public void matchesToString() {
        assertEquals("matches(\"\\\\s+\")", describe(new Matches("\\s+")));
    }

}
