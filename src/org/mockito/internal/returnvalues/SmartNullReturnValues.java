package org.mockito.internal.returnvalues;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

import org.mockito.configuration.ReturnValues;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.configuration.DefaultReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class SmartNullReturnValues implements ReturnValues {

    private final ReturnValues delegate = new DefaultReturnValues();

    public Object valueFor(InvocationOnMock invocation) {
        final Class<?> type = invocation.getMethod().getReturnType();
        if (type.isPrimitive()) {
            return delegate.valueFor(invocation);
        }
        //TODO change from UndesiredInvocation
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, new InvocationHandler() {
            Exception whenCreated = new UndesiredInvocation("Unstubbed method was invoked here");
            //TODO create mock here
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                throw new SmartNullPointerException("oops", whenCreated);
            }});
    }
}
