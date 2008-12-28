package org.mockito.internal.returnvalues;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.mockito.configuration.ReturnValues;
import org.mockito.exceptions.cause.BecauseThisMethodWasNotStubbed;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.configuration.DefaultReturnValues;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;

public class SmartNullReturnValues implements ReturnValues {

    private final ReturnValues delegate = new DefaultReturnValues();

    public Object valueFor(InvocationOnMock invocation) {
        Object defaultReturnValue = delegate.valueFor(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        Class<?> type = invocation.getMethod().getReturnType();
        if (ClassImposterizer.INSTANCE.canImposterise(type)) {
            return ClassImposterizer.INSTANCE.imposterise(new MethodInterceptor() {
                Exception whenCreated = new BecauseThisMethodWasNotStubbed("\nBecause this method was not stubbed:");
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    throw new SmartNullPointerException("\nYou have a NullPointerException here:", whenCreated);
                }}, type);
        }
        return null;
    }
}