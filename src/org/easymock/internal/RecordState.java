/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.mockito.internal.*;
import org.mockito.internal.matchers.IArgumentMatcher;

public class RecordState implements IMocksControlState {

    protected ExpectedInvocation lastInvocation;

    protected boolean lastInvocationUsed = true;

    protected Result lastResult;

    protected IMocksBehavior behavior;

    public RecordState(IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    public void assertRecordState() {
    }

    public java.lang.Object invoke(Invocation invocation) {
        closeMethod();
        List<IArgumentMatcher> lastMatchers = LastArguments.pullMatchers();
        lastInvocation = new ExpectedInvocation(invocation, lastMatchers);
        lastInvocationUsed = false;
        return ToTypeMappings.emptyReturnValueFor(invocation.getMethod().getReturnType());
    }

    public void replay() {
        closeMethod();
        if (LastArguments.pullMatchers() != null) {
            throw new IllegalStateException("matcher calls were used outside expectations");
        }
    }

    public void verify() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "calling verify is not allowed in record state"));
    }

    public void andReturn(Object value) {
        requireMethodCall("return value");
        value = convertNumberClassIfNeccessary(value);
        requireAssignable(value);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createReturnResult(value);
    }

    public void andThrow(Throwable throwable) {
        requireMethodCall("Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createThrowResult(throwable);
    }
    
    public void andAnswer(IAnswer answer) {
        requireMethodCall("answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        lastResult = Result.createAnswerResult(answer);
    }

    public void andStubReturn(Object value) {
        requireMethodCall("stub return value");
        value = convertNumberClassIfNeccessary(value);
        requireAssignable(value);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createReturnResult(value));
        lastInvocationUsed = true;
    }

    public void asStub() {
        requireMethodCall("stub behavior");
        requireVoidMethod();
        behavior.addStub(lastInvocation, Result.createReturnResult(null));
        lastInvocationUsed = true;
    }

    public void andStubThrow(Throwable throwable) {
        requireMethodCall("stub Throwable");
        requireValidThrowable(throwable);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createThrowResult(throwable));
        lastInvocationUsed = true;
    }

    public void andStubAnswer(IAnswer answer) {
        requireMethodCall("stub answer");
        requireValidAnswer(answer);
        if (lastResult != null) {
            times(MocksControl.ONCE);
        }
        behavior.addStub(lastInvocation, Result.createAnswerResult(answer));
        lastInvocationUsed = true;
    }

    public void times(Range range) {
        requireMethodCall("times");
        requireLastResultOrVoidMethod();

        behavior.addExpected(lastInvocation, lastResult != null ? lastResult
                : Result.createReturnResult(null), range);
        lastInvocationUsed = true;
        lastResult = null;
    }

    protected Object createNumberObject(Object value, Class returnType) {
        if (!(value instanceof Number)) {
            return value;
        }
        Number number = (Number) value;
        if (returnType.equals(Byte.TYPE)) {
            return number.byteValue();
        } else if (returnType.equals(Short.TYPE)) {
            return number.shortValue();
        } else if (returnType.equals(Character.TYPE)) {
            return (char) number.intValue();
        } else if (returnType.equals(Integer.TYPE)) {
            return number.intValue();
        } else if (returnType.equals(Long.TYPE)) {
            return number.longValue();
        } else if (returnType.equals(Float.TYPE)) {
            return number.floatValue();
        } else if (returnType.equals(Double.TYPE)) {
            return number.doubleValue();
        } else {
            return number;
        }
    }

    protected Object convertNumberClassIfNeccessary(Object o) {
        Class returnType = lastInvocation.getMethod().getReturnType();
        return createNumberObject(o, returnType);
    }

    protected void closeMethod() {
        if (lastInvocationUsed && lastResult == null) {
            return;
        }
        if (!isLastResultOrVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "missing behavior definition for the preceeding method call "
                            + lastInvocation.toString()));
        }
        this.times(MockControl.ONE);
    }

    protected void requireMethodCall(String failMessage) {
        if (lastInvocation == null) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "method call on the mock needed before setting "
                            + failMessage));
        }
    }

    protected void requireAssignable(Object returnValue) {
        if (lastMethodIsVoidMethod()) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "void method cannot return a value"));
        }
        if (returnValue == null) {
            return;
        }
        Class<?> returnedType = lastInvocation.getMethod().getReturnType();
        if (returnedType.isPrimitive()) {
            returnedType = null;//ToTypeMappings.primitiveToWrapperType.get(returnedType);
        }
        if (!returnedType.isAssignableFrom(returnValue.getClass())) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "incompatible return value type"));
        }
    }

    protected void requireValidThrowable(Throwable throwable) {
        if (throwable == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "null cannot be thrown"));
        if (isValidThrowable(throwable))
            return;

        throw new RuntimeExceptionWrapper(new IllegalArgumentException(
                "last method called on mock cannot throw "
                        + throwable.getClass().getName()));
    }

    protected void requireValidAnswer(IAnswer answer) {
        if (answer == null)
            throw new RuntimeExceptionWrapper(new NullPointerException(
                    "answer object must not be null"));
    }

    protected void requireLastResultOrVoidMethod() {
        if (isLastResultOrVoidMethod()) {
            return;
        }
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "last method called on mock is not a void method"));
    }

    protected void requireVoidMethod() {
        if (lastMethodIsVoidMethod()) {
            return;
        }
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "last method called on mock is not a void method"));
    }

    protected boolean isLastResultOrVoidMethod() {
        return lastResult != null || lastMethodIsVoidMethod();
    }

    protected boolean lastMethodIsVoidMethod() {
        Class returnType = lastInvocation.getMethod().getReturnType();
        return returnType.equals(Void.TYPE);
    }

    protected boolean isValidThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return true;
        }
        if (throwable instanceof Error) {
            return true;
        }
        Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass))
                return true;
        }
        return false;
    }

    public void checkOrder(boolean value) {
        closeMethod();
        behavior.checkOrder(value);
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        behavior.setDefaultMatcher(matcher);
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        requireMethodCall("matcher");
        behavior.setMatcher(lastInvocation.getMethod(), matcher);
    }
}