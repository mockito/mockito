/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StubTest {
    private IMethods mock;

    @Before
    public void setup() {
        mock = createStrictMock(IMethods.class);
    }

    @Test
    public void stub() {
        mock.simpleMethodWithArgument("1");
        expectLastCall().anyTimes();
        mock.simpleMethodWithArgument("2");
        expectLastCall().anyTimes();
        mock.simpleMethodWithArgument("3");
        expectLastCall().asStub();

        replay(mock);

        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("1");
        mock.simpleMethodWithArgument("2");
        mock.simpleMethodWithArgument("3");
        mock.simpleMethodWithArgument("3");

        verify(mock);

    }

    @Test
    public void stubWithReturnValue() {
        expect(mock.oneArg("1")).andReturn("A").andStubReturn("B");
        expect(mock.oneArg("2")).andThrow(new IllegalArgumentException())
                .andStubThrow(new IllegalStateException());

        replay(mock);

        assertEquals("A", mock.oneArg("1"));
        assertEquals("B", mock.oneArg("1"));
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (IllegalArgumentException ignored) {
        }
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (IllegalStateException ignored) {
        }
        assertEquals("B", mock.oneArg("1"));
        try {
            mock.oneArg("2");
        } catch (IllegalStateException ignored) {
        }
        verify(mock);
    }

}
