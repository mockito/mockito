/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.ArgumentMatcher;
import org.mockito.internal.matchers.Matches;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.StartsWith;

@SuppressWarnings("unchecked")
public class MatchersToStringTest extends TestBase {
    private StringBuilder buffer;

    @Before
    public void setup() {
        buffer = new StringBuilder();
    }

    @Test
    public void sameToStringWithString() {
        new Same("X").appendTo(buffer);
        assertEquals("same(\"X\")", buffer.toString());

    }

    @Test
    public void nullToString() {
        Null.NULL.appendTo(buffer);
        assertEquals("isNull()", buffer.toString());
    }

    @Test
    public void notNullToString() {
        NotNull.NOT_NULL.appendTo(buffer);
        assertEquals("notNull()", buffer.toString());
    }

    @Test
    public void anyToString() {
        Any.ANY.appendTo(buffer);
        assertEquals("<any>", buffer.toString());
    }

    @Test
    public void sameToStringWithChar() {
        new Same('x').appendTo(buffer);
        assertEquals("same('x')", buffer.toString());
    }

    @Test
    public void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Same(o).appendTo(buffer);
        assertEquals("same(X)", buffer.toString());
    }

    @Test
    public void equalsToStringWithString() {
        new Equals("X").appendTo(buffer);
        assertEquals("\"X\"", buffer.toString());

    }

    @Test
    public void equalsToStringWithChar() {
        new Equals('x').appendTo(buffer);
        assertEquals("'x'", buffer.toString());
    }

    @Test
    public void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Equals(o).appendTo(buffer);
        assertEquals("X", buffer.toString());
    }

    @Test
    public void orToString() {
        List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new Or(matchers).appendTo(buffer);
        assertEquals("or(1, 2)", buffer.toString());
    }

    @Test
    public void notToString() {
        new Not(new Equals(1)).appendTo(buffer);
        assertEquals("not(1)", buffer.toString());
    }

    @Test
    public void andToString() {
        List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new And(matchers).appendTo(buffer);
        assertEquals("and(1, 2)", buffer.toString());
    }

    @Test
    public void startsWithToString() {
        new StartsWith("AB").appendTo(buffer);
        assertEquals("startsWith(\"AB\")", buffer.toString());
    }

    @Test
    public void endsWithToString() {
        new EndsWith("AB").appendTo(buffer);
        assertEquals("endsWith(\"AB\")", buffer.toString());
    }

    @Test
    public void containsToString() {
        new Contains("AB").appendTo(buffer);
        assertEquals("contains(\"AB\")", buffer.toString());
    }

    @Test
    public void findToString() {
        new Find("\\s+").appendTo(buffer);
        assertEquals("find(\"\\\\s+\")", buffer.toString());
    }

    @Test
    public void matchesToString() {
        new Matches("\\s+").appendTo(buffer);
        assertEquals("matches(\"\\\\s+\")", buffer.toString());
    }

}
