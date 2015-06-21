/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.MockitoMatcher;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatchersToStringTest extends TestBase {

    @Test
    public void sameToStringWithString() {
        assertEquals("same(\"X\")", new Same("X").describe());
    }

    @Test
    public void nullToString() {
        assertEquals("isNull()", Null.NULL.describe());
    }

    @Test
    public void notNullToString() {
        assertEquals("notNull()", NotNull.NOT_NULL.describe());
    }

    @Test
    public void anyToString() {
        assertEquals("<any>", Any.ANY.describe());
    }

    @Test
    public void sameToStringWithChar() {
        assertEquals("same('x')", new Same('x').describe());
    }

    @Test
    public void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("same(X)", new Same(o).describe());
    }

    @Test
    public void equalsToStringWithString() {
        assertEquals("\"X\"", new Equals("X").describe());

    }

    @Test
    public void equalsToStringWithChar() {
        assertEquals("'x'", new Equals('x').describe());
    }

    @Test
    public void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        assertEquals("X", new Equals(o).describe());
    }

    @Test
    public void orToString() {
        List<MockitoMatcher> matchers = new ArrayList<MockitoMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        assertEquals("or(1, 2)", new Or(matchers).describe());
    }

    @Test
    public void notToString() {
        assertEquals("not(1)", new Not(new Equals(1)).describe());
    }

    @Test
    public void andToString() {
        List<MockitoMatcher> matchers = new ArrayList<MockitoMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        assertEquals("and(1, 2)", new And(matchers).describe());
    }

    @Test
    public void startsWithToString() {
        assertEquals("startsWith(\"AB\")", new StartsWith("AB").describe());
    }

    @Test
    public void endsWithToString() {
        assertEquals("endsWith(\"AB\")", new EndsWith("AB").describe());
    }

    @Test
    public void containsToString() {
        assertEquals("contains(\"AB\")", new Contains("AB").describe());
    }

    @Test
    public void findToString() {
        assertEquals("find(\"\\\\s+\")", new Find("\\s+").describe());
    }

    @Test
    public void matchesToString() {
        assertEquals("matches(\"\\\\s+\")", new Matches("\\s+").describe());
    }

}
