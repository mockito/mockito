/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.EndsWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.Matches;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.mockito.internal.matchers.Or;
import org.mockito.internal.matchers.Same;
import org.mockito.internal.matchers.StartsWith;

@SuppressWarnings("unchecked")
public class MatchersToStringTest extends TestBase {
    private Description description;

    @Before
    public void setup() {
        description = new StringDescription();
    }

    @Test
    public void sameToStringWithString() {
        new Same("X").describeTo(description);
        assertEquals("same(\"X\")", description.toString());

    }

    @Test
    public void nullToString() {
        Null.NULL.describeTo(description);
        assertEquals("isNull()", description.toString());
    }

    @Test
    public void notNullToString() {
        NotNull.NOT_NULL.describeTo(description);
        assertEquals("notNull()", description.toString());
    }

    @Test
    public void anyToString() {
        Any.ANY.describeTo(description);
        assertEquals("<any>", description.toString());
    }

    @Test
    public void sameToStringWithChar() {
        new Same('x').describeTo(description);
        assertEquals("same('x')", description.toString());
    }

    @Test
    public void sameToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Same(o).describeTo(description);
        assertEquals("same(X)", description.toString());
    }

    @Test
    public void equalsToStringWithString() {
        new Equals("X").describeTo(description);
        assertEquals("\"X\"", description.toString());

    }

    @Test
    public void equalsToStringWithChar() {
        new Equals('x').describeTo(description);
        assertEquals("'x'", description.toString());
    }

    @Test
    public void equalsToStringWithObject() {
        Object o = new Object() {
            @Override
            public String toString() {
                return "X";
            }
        };
        new Equals(o).describeTo(description);
        assertEquals("X", description.toString());
    }

    @Test
    public void orToString() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new Or(matchers).describeTo(description);
        assertEquals("or(1, 2)", description.toString());
    }

    @Test
    public void notToString() {
        new Not(new Equals(1)).describeTo(description);
        assertEquals("not(1)", description.toString());
    }

    @Test
    public void andToString() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        new And(matchers).describeTo(description);
        assertEquals("and(1, 2)", description.toString());
    }

    @Test
    public void startsWithToString() {
        new StartsWith("AB").describeTo(description);
        assertEquals("startsWith(\"AB\")", description.toString());
    }

    @Test
    public void endsWithToString() {
        new EndsWith("AB").describeTo(description);
        assertEquals("endsWith(\"AB\")", description.toString());
    }

    @Test
    public void containsToString() {
        new Contains("AB").describeTo(description);
        assertEquals("contains(\"AB\")", description.toString());
    }

    @Test
    public void findToString() {
        new Find("\\s+").describeTo(description);
        assertEquals("find(\"\\\\s+\")", description.toString());
    }

    @Test
    public void matchesToString() {
        new Matches("\\s+").describeTo(description);
        assertEquals("matches(\"\\\\s+\")", description.toString());
    }

}
