/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;

import org.mockito.internal.matchers.*;

public class Invocation {

    private boolean verified;
    
    private final int sequenceNumber;
    private final Object mock;
    private final Method method;
    private final Object[] arguments;

    private boolean verifiedInOrder;

    public Invocation(Object mock, Method method, Object[] args, int sequenceNumber) {
        this.mock = mock;
        this.method = method;
        this.arguments = expandVarArgs(method.isVarArgs(), args);
        this.sequenceNumber = sequenceNumber;
    }

    private static Object[] expandVarArgs(final boolean isVarArgs,
            final Object[] args) {
        if (!isVarArgs || isVarArgs && args[args.length - 1] != null
                && !args[args.length - 1].getClass().isArray()) {
            return args == null ? new Object[0] : args;
        }
        Object[] varArgs = ArrayEquals.createObjectArray(args[args.length - 1]);
        final int nonVarArgsCount = args.length - 1;
        final int varArgsCount = varArgs.length;
        Object[] newArgs = new Object[nonVarArgsCount + varArgsCount];
        System.arraycopy(args, 0, newArgs, 0, nonVarArgsCount);
        System.arraycopy(varArgs, 0, newArgs, nonVarArgsCount, varArgsCount);
        return newArgs;
    }

    public Object getMock() {
        return mock;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    //TODO add test that makes sure sequenceNumber doesnt take part in equals()
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass()))
            return false;

        Invocation other = (Invocation) o;

        return this.mock.equals(other.mock) && this.method.equals(other.method)
                && this.equalArguments(other.arguments);
    }

    public int hashCode() {
        return 1;
    }

    private boolean equalArguments(Object[] arguments) {
        if (this.arguments.length != arguments.length) {
            return false;
        }
        for (int i = 0; i < this.arguments.length; i++) {
            Object myArgument = this.arguments[i];
            Object otherArgument = arguments[i];

            if (isPrimitiveParameter(i)) {
                if (!myArgument.equals(otherArgument)) {
                    return false;
                }
            } else {
                if (myArgument != otherArgument) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPrimitiveParameter(int parameterPosition) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.isVarArgs()) {
            parameterPosition = Math.min(parameterPosition,
                    parameterTypes.length - 1);
        }
        return parameterTypes[parameterPosition].isPrimitive();
    }

    public String getMockAndMethodName() {
        String mockName = Namer.nameForMock(mock);
        return mockName + "." + method.getName();
    }
    
    public String toString() {
        //TODO separate unit test?
        StringBuffer result = new StringBuffer();
        result.append(getMockAndMethodName());
        result.append("(");
        for (Object arg : this.arguments) {
            //TODO lil bit hacky way of using Equals matcher
            new Equals(arg).appendTo(result);
            result.append(", ");
        }
        return result.toString().replaceFirst(", $", "").concat(")");
    }
    
    public String toStringWithArgumentTypes() {
        //TODO separate unit test?
        StringBuilder result = new StringBuilder();
        result.append(getMockAndMethodName());
        result.append("(");
        for (Class<?> paramType : getMethod().getParameterTypes()) {
            result.append(paramType);
            result.append(", ");
        } 
        return result.toString().replaceFirst(", $", "").concat(")");
    }

    public void markVerified() {
        verified = true;
    }

    public boolean isVerified() {
        return verified;
    }
    
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void markVerifiedInOrder() {
        this.verifiedInOrder = true;
        
    }

    public boolean isVerifiedInOrder() {
        return verifiedInOrder;
    }
}
