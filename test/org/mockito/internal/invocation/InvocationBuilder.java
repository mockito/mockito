/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class InvocationBuilder {

    private String methodName = "simpleMethod";
    private int sequenceNumber = 0;
    private Object[] args = new Object[] {};
    private Object mock = "mock";
    private Method method;

    public Invocation toInvocation() {
        if (method != null) {
            return new Invocation(mock, method, args, sequenceNumber);
        }
        
        Method m;
        List<Class> argTypes = new LinkedList<Class>();
        for (Object arg : args) {
            if (arg == null) {
                argTypes.add(Object.class);
            } else {
                argTypes.add(arg.getClass());
            }
        }
        
        try {
            m = IMethods.class.getMethod(methodName, argTypes.toArray(new Class[argTypes.size()]));
        } catch (Exception e) {
            throw new RuntimeException("builder only creates invocations of IMethods interface", e);
        }
        return new Invocation(mock, m, args, sequenceNumber);
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
    
    public InvocationBuilder arg(Object o) {
        this.args = new Object[] {o};
        return this;
    }

    public InvocationBuilder mock(Object mock) {
        this.mock = mock;
        return this;
    }

    public InvocationBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public InvocationMatcher toInvocationMatcher() {
        return new InvocationMatcher(toInvocation());
    }

    public InvocationBuilder simpleMethod() {
        return this.method("simpleMethod");
    }
    
    public InvocationBuilder differentMethod() {
        return this.method("differentMethod");
    }
}
