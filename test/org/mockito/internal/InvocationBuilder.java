package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.mockito.usage.IMethods;

@SuppressWarnings("unchecked")
public class InvocationBuilder {

    private String methodName = "simpleMethod";
    private int sequenceNumber = 0;
    private Object[] args = new Object[] {};

    public Invocation toInvocation() {
        Method method;
        List<Class> argTypes = new LinkedList<Class>();
        for (Object arg : args) {
            argTypes.add(arg.getClass());
        }
        
        try {
            method = IMethods.class.getMethod(methodName, argTypes.toArray(new Class[argTypes.size()]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Invocation i = new Invocation("mock", method, args, sequenceNumber);
        return i;
    }

    public InvocationBuilder method(String methodName) {
        this.methodName  = methodName;
        return this;
    }

    public InvocationBuilder seq(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public InvocationBuilder args(Object ... args) {
        this.args = args;
        return this;
    }
}
