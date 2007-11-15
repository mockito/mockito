/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

/**
 * Same as UsageExpectAndThrowTest except that each mocked method is called
 * twice to make sure the defaulting works fine.
 * 
 * @author Henri Tremblay
 */
public class UsageExpectAndDefaultThrowTest {
    private MockControl<IMethods> control;

    private IMethods mock;

    private static RuntimeException EXCEPTION = new RuntimeException();

    @Before
    public void setup() {
        control = MockControl.createControl(IMethods.class);
        mock = control.getMock();
    }

    @Test
    public void booleanType() {
        control
                .expectAndDefaultThrow(mock.booleanReturningMethod(4),
                        EXCEPTION);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    @Test
    public void longType() {
        control.expectAndDefaultThrow(mock.longReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    @Test
    public void floatType() {
        control.expectAndDefaultThrow(mock.floatReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    @Test
    public void doubleType() {
        control.expectAndDefaultThrow(mock.doubleReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    @Test
    public void object() {
        control.expectAndDefaultThrow(mock.objectReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
            fail();
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    @Test
    public void throwableAndDefaultThrowable() throws Exception {

        mock.oneArg("1");

        expectLastCall().andThrow(new IllegalArgumentException());
        control.setDefaultThrowable(new IllegalStateException());

        control.replay();

        try {
            mock.oneArg("1");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            mock.oneArg("1");
        } catch (IllegalStateException ignored) {
        }
        try {
            mock.oneArg("2");
        } catch (IllegalStateException ignored) {
        }
        control.verify();
    }

}
