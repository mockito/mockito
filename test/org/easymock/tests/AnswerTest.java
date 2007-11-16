/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

public class AnswerTest {

    private IMethods mock;

    @Before
    public void setUp() {
        mock = createMock(IMethods.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void answer() {
        IAnswer firstAnswer = new IAnswer() {
            public Object answer() {
                assertEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        IAnswer secondAnswer = new IAnswer() {
            public Object answer() {
                assertEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                throw new IllegalStateException("Call answered");
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andAnswer(firstAnswer)
                .andReturn("Second call").andAnswer(secondAnswer).andReturn(
                        "Fourth call");

        replay(mock);

        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Second call", mock.threeArgumentMethod(1, "2", "3"));
        try {
            mock.threeArgumentMethod(1, "2", "3");
            fail();
        } catch (IllegalStateException expected) {
            assertEquals("Call answered", expected.getMessage());
        }
        assertEquals("Fourth call", mock.threeArgumentMethod(1, "2", "3"));

        verify(mock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stubAnswer() {
        IAnswer firstAnswer = new IAnswer() {
            public Object answer() {
                assertEquals(new Object[] { 1, "2", "3" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        IAnswer secondAnswer = new IAnswer() {
            public Object answer() {
                assertEquals(new Object[] { 1, "2", "4" },
                        getCurrentArguments());
                return "Call answered";
            }
        };

        expect(mock.threeArgumentMethod(1, "2", "3")).andReturn(42)
                .andStubAnswer(firstAnswer);
        expect(mock.threeArgumentMethod(1, "2", "4")).andStubAnswer(
                secondAnswer);

        replay(mock);

        assertEquals(42, mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "4"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));
        assertEquals("Call answered", mock.threeArgumentMethod(1, "2", "3"));

        verify(mock);
    }

    @Test
    public void nullAnswerNotAllowed() {
        try {
            expect(mock.threeArgumentMethod(1, "2", "3")).andAnswer(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("answer object must not be null", expected
                    .getMessage());
        }
    }

    @Test
    public void nullStubAnswerNotAllowed() {
        try {
            expect(mock.threeArgumentMethod(1, "2", "3")).andStubAnswer(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("answer object must not be null", expected
                    .getMessage());
        }
    }

}
