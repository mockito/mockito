/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.matchers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.internal.matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.*;
import org.mockito.matchers.*;

public class MatchersToStringTest {
    private StringBuffer buffer;

    @Before
    public void setup() {
        buffer = new StringBuffer();
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
        List<IArgumentMatcher> matchers = new ArrayList<IArgumentMatcher>();
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
        List<IArgumentMatcher> matchers = new ArrayList<IArgumentMatcher>();
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
