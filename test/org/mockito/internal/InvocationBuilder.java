package org.mockito.internal;

import java.lang.reflect.Method;

import org.mockito.usage.IMethods;

public class InvocationBuilder {

    private String methodName = "simpleMethod";
    private int sequenceNumber = 0;

    public Invocation toInvocation() {
        Method method;
        try {
            method = IMethods.class.getMethod(methodName, new Class[] {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Invocation i = new Invocation("mock", method, new Object[] {}, sequenceNumber);
        return i;
    }

    public InvocationBuilder m(String methodName) {
        this.methodName  = methodName;
        return this;
    }

    public InvocationBuilder s(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }
}
