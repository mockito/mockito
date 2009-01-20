/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

/**
 * Method call on a mock object. 
 * <p>
 * Contains sequence number which should be
 * globally unique and is used for verification in order.
 * <p>
 * Contains stack trace of invocation
 */
@SuppressWarnings("unchecked")
public class Invocation implements PrintableInvocation, InvocationOnMock, CanPrintInMultilines {

    private static final int MAX_LINE_LENGTH = 45;
    private final int sequenceNumber;
    private final Object mock;
    private final Method method;
    private final Object[] arguments;
    private final HasStackTrace stackTrace;

    private boolean verified;
    private boolean verifiedInOrder;

    public Invocation(Object mock, Method method, Object[] args, int sequenceNumber) {
        this.mock = mock;
        this.method = method;
        this.arguments = expandVarArgs(method.isVarArgs(), args);
        this.sequenceNumber = sequenceNumber;
        this.stackTrace = new MockitoException("");
    }

    //expands array varArgs that are given by runtime (1, [a, b]) into true varArgs (1, a, b);
    private static Object[] expandVarArgs(final boolean isVarArgs,
            final Object[] args) {
        if (!isVarArgs || isVarArgs && args[args.length - 1] != null
                && !args[args.length - 1].getClass().isArray()) {
            return args == null ? new Object[0] : args;
        }
            
        final int nonVarArgsCount = args.length - 1;
        Object[] varArgs;  
        if (args[nonVarArgsCount] == null) {
            //in case someone deliberately passed null varArg array
            varArgs = new Object[] {null};
        } else {
            varArgs = ArrayEquals.createObjectArray(args[nonVarArgsCount]);
        }
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
        this.markVerified();
        this.verifiedInOrder = true;
    }

    public boolean isVerifiedInOrder() {
        return verifiedInOrder;
    }
    
    public HasStackTrace getStackTrace() {
        return stackTrace;
    }

    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }

        Invocation other = (Invocation) o;

        return this.mock.equals(other.mock) && this.method.equals(other.method)
                && this.equalArguments(other.arguments);
    }

    private boolean equalArguments(Object[] arguments) {
        return Arrays.equals(arguments, this.arguments);
    }

    public int hashCode() {
        throw new RuntimeException("hashCode() is not implemented");
    }
    
    public String toString() {
        return toString(argumentsToMatchers(), false);
    }

    public boolean printsInMultilines() {
        return toString().contains("\n");
    }

    public String toMultilineString() {
        return toString(argumentsToMatchers(), true);
    }    

    protected String toString(List<Matcher> matchers, boolean forceMultiline) {
        String method = qualifiedMethodName();
        String invocation = method + getArgumentsLine(matchers);
        if (forceMultiline || (!matchers.isEmpty() && invocation.length() > MAX_LINE_LENGTH)) {
            return method + getArgumentsBlock(matchers);
        } else {
            return invocation;
        }
    }

    private String qualifiedMethodName() {
        return MockUtil.getMockName(mock) + "." + method.getName();
    }

    private String getArgumentsLine(List<Matcher> matchers) {
        Description result = new StringDescription();
        result.appendList("(", ", ", ");", matchers);
        return result.toString();
    }
    
    private String getArgumentsBlock(List<Matcher> matchers) {
        Description result = new StringDescription();
        result.appendList("(\n    ", ",\n    ", "\n);", matchers);
        return result.toString();
    }
    
    protected List<Matcher> argumentsToMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>(arguments.length);
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals(arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }

    public static boolean isToString(InvocationOnMock invocation) {
        return isToString(invocation.getMethod());
    }
    
    public static boolean isToString(Method method) {
        return method.getReturnType() == String.class 
        && method.getParameterTypes().length == 0 
        && method.getName().equals("toString");
    }

    public boolean isValidException(Throwable throwable) {
        Class<?>[] exceptions = this.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isValidReturnType(Class clazz) {
        if (method.getReturnType().isPrimitive()) {
            return Primitives.primitiveTypeOf(clazz) == method.getReturnType();
        } else {
            return method.getReturnType().isAssignableFrom(clazz);
        }
    }

    public boolean isVoid() {
        return this.method.getReturnType() == Void.TYPE;
    }

    public String printMethodReturnType() {
        return method.getReturnType().getSimpleName();
    }

    public String getMethodName() {
        return method.getName();
    }

    public boolean returnsPrimitive() {
        return method.getReturnType().isPrimitive();
    }
}