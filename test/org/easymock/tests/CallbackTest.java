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

public class CallbackTest {

    private IMethods mock;

    private static class Callback<T> implements IAnswer<T> {
        private int callCount;

        private T result;

        public Callback(T result) {
            this.result = result;
        }

        public void run() {
        }

        public int getCallCount() {
            return callCount;
        }

        public T answer() throws Throwable {
            callCount++;
            return result;
        }
    }

    @Before
    public void setUp() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void callback() {
        Callback<String> c1 = new Callback<String>("1");
        Callback<Object> c2 = new Callback<Object>(null);
        Callback<Object> c3 = new Callback<Object>(null);

        expect(mock.oneArg("2")).andAnswer(c1).times(2);
        mock.simpleMethodWithArgument("One");
        expectLastCall().andAnswer(c2);
        mock.simpleMethodWithArgument("Two");
        expectLastCall().andAnswer(c3).times(2);

        replay(mock);

        mock.oneArg("2");
        mock.oneArg("2");
        try {
            mock.oneArg("2");
        } catch (AssertionError ignored) {
        }
        try {
            mock.simpleMethodWithArgument("Two");
        } catch (AssertionError ignored) {
        }
        mock.simpleMethodWithArgument("One");
        try {
            mock.simpleMethodWithArgument("One");
        } catch (AssertionError ignored) {
        }
        mock.simpleMethodWithArgument("Two");
        mock.simpleMethodWithArgument("Two");
        try {
            mock.simpleMethodWithArgument("Two");
        } catch (AssertionError ignored) {
        }
        verify(mock);

        assertEquals(2, c1.getCallCount());
        assertEquals(1, c2.getCallCount());
        assertEquals(2, c3.getCallCount());
    }
}
