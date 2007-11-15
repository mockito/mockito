/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.IAnswer;
import org.easymock.tests.IMethods;
import org.junit.Before;
import org.junit.Test;

public class CallbackAndArgumentsTest {

    private IMethods mock;

    @Before
    public void setUp() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void callbackGetsArguments() {

        final StringBuffer buffer = new StringBuffer();

        mock.simpleMethodWithArgument((String) notNull());
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() {
                buffer.append((String) getCurrentArguments()[0]);
                return null;
            }
        }).times(2);

        replay(mock);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        verify(mock);

        assertEquals("12", buffer.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void currentArgumentsFailsOutsideCallbacks() {
        getCurrentArguments();
    }

    @Test
    public void callbackGetsArgumentsEvenIfAMockCallsAnother() {

        final StringBuffer buffer = new StringBuffer();

        final IMethods mock2 = createStrictMock(IMethods.class);
        mock2.simpleMethod();
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() {
                // empty, only needed to force deletion of arguments
                return null;
            }
        }).times(2);

        mock.simpleMethodWithArgument((String) notNull());
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() {
                mock2.simpleMethod();
                buffer.append((String) getCurrentArguments()[0]);
                return null;
            }
        }).times(2);

        replay(mock);
        replay(mock2);

        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");

        verify(mock);
        verify(mock2);

        assertEquals("12", buffer.toString());
    }
}
