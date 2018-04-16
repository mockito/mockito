/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.SerializableSupplier;
import org.mockito.internal.util.Supplier;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfReturnValue;

public class Returns implements Answer<Object>, ValidableAnswer, Serializable {

    private static final long serialVersionUID = -6245608253574215396L;

    private final SerializableSupplier valueSupplier;
    private final Class<?> typeHint;
    private final boolean returnsNull;
    private final boolean singleReturnInstance;

    public Returns(Object value) {
        this.singleReturnInstance = true;
        valueSupplier = (SerializableSupplier) () -> value;
        if (value == null) {
            typeHint = null;
            returnsNull = true;
        } else {
            typeHint = value.getClass();
            returnsNull = false;
        }
    }

    public Returns(SerializableSupplier valueSupplier, Class typeHint) {
        this.singleReturnInstance = false;
        this.valueSupplier = valueSupplier;
        this.typeHint = typeHint;
        returnsNull = false;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return valueSupplier.get();
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        if (invocationInfo.isVoid()) {
            throw cannotStubVoidMethodWithAReturnValue(invocationInfo.getMethodName());
        }

        if (returnsNull() && invocationInfo.returnsPrimitive()) {
            throw wrongTypeOfReturnValue(invocationInfo.printMethodReturnType(), "null", invocationInfo.getMethodName());
        }

        if (!returnsNull() && !invocationInfo.isValidReturnType(returnType())) {
            throw wrongTypeOfReturnValue(invocationInfo.printMethodReturnType(), printReturnType(), invocationInfo.getMethodName());
        }
    }

    private String printReturnType() {
        return typeHint.getSimpleName();
    }

    private Class<?> returnType() {
        return typeHint;
    }

    private boolean returnsNull() {
        return returnsNull;
    }

    @Override
    public String toString() {
        Object returnObject = null;
        if (singleReturnInstance) {
            returnObject = valueSupplier.get();
        } else {
            returnObject = "Supplier of class " + typeHint;
        }
        return "Returns: " + returnObject;
    }
}
