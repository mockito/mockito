/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class UsageVarargTest {

    MockControl<IVarArgs> control;

    IVarArgs mock;

    @Before
    public void setup() {
        control = MockControl.createStrictControl(IVarArgs.class);
        mock = control.getMock();
    }

    @Test
    public void varargObjectAccepted() {
        mock.withVarargsString(1, "1");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsObject(3, "1");
        mock.withVarargsObject(4, "1", "2");

        control.replay();
        mock.withVarargsString(1, "1");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsString(2, "1", "2");
        mock.withVarargsObject(3, "1");
        mock.withVarargsObject(4, "1", "2");
        control.verify();
    }

    @Test
    public void varargBooleanAccepted() {
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);

        control.replay();
        mock.withVarargsBoolean(1, true);
        mock.withVarargsBoolean(2, true, false);
        control.verify();
    }

    @Test
    public void varargByteAccepted() {
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);

        control.replay();
        mock.withVarargsByte(1, (byte) 1);
        mock.withVarargsByte(2, (byte) 1, (byte) 2);
        control.verify();
    }

    @Test
    public void varargCharAccepted() {
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');

        control.replay();
        mock.withVarargsChar(1, 'a');
        mock.withVarargsChar(1, 'a', 'b');
        control.verify();
    }

    @Test
    public void varargDoubleAccepted() {
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);

        control.replay();
        mock.withVarargsDouble(1, 1.0d);
        mock.withVarargsDouble(1, 1.0d, 2.0d);
        control.verify();
    }

    @Test
    public void varargFloatAccepted() {
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);

        control.replay();
        mock.withVarargsFloat(1, 1.0f);
        mock.withVarargsFloat(1, 1.0f, 2.0f);
        control.verify();
    }

    @Test
    public void varargIntAccepted() {
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);

        control.replay();
        mock.withVarargsInt(1, 1);
        mock.withVarargsInt(1, 1, 2);
        control.verify();
    }

    @Test
    public void varargLongAccepted() {
        mock.withVarargsLong(1, (long) 1);
        mock.withVarargsLong(1, 1, 2);

        control.replay();
        mock.withVarargsLong(1, (long) 1);
        mock.withVarargsLong(1, 1, 2);
        control.verify();
    }

    @Test
    public void varargShortAccepted() {
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);

        control.replay();
        mock.withVarargsShort(1, (short) 1);
        mock.withVarargsShort(1, (short) 1, (short) 2);
        control.verify();
    }
    
    @Test
    public void varargAcceptedIfArrayIsGiven() {
        IVarArgs object = (IVarArgs) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { IVarArgs.class }, new InvocationHandler() {
        
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                return null;
            }
        });
        object.withVarargsObject(1);
        object.withVarargsObject(1, (Object) null);
        object.withVarargsObject(1, (Object[]) null);
        object.withVarargsObject(1, (Object[]) new Object[0] );
        object.withVarargsObject(1, false);
        object.withVarargsObject(1, new boolean[] {true, false});
    }
}