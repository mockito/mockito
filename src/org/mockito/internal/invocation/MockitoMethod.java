package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.mockito.exceptions.base.MockitoException;

public class MockitoMethod implements Serializable {

    private static final long serialVersionUID = 6005610965006048445L;
    private Class<?> declaringClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;

    public MockitoMethod(Method method) {
        declaringClass = method.getDeclaringClass();
        methodName = method.getName();
        parameterTypes = method.getParameterTypes();
        returnType = method.getReturnType();
    }

    @Override
    public int hashCode() {
        throw new RuntimeException("hashCode() not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MockitoMethod other = (MockitoMethod) obj;
        if (declaringClass == null) {
            if (other.declaringClass != null)
                return false;
        } else if (!declaringClass.equals(other.declaringClass))
            return false;
        if (methodName == null) {
            if (other.methodName != null)
                return false;
        } else if (!methodName.equals(other.methodName))
            return false;
        if (!Arrays.equals(parameterTypes, other.parameterTypes))
            return false;
        if (returnType == null) {
            if (other.returnType != null)
                return false;
        } else if (!returnType.equals(other.returnType))
            return false;
        return true;
    }

    public Method getMethod() {
        try {
            return declaringClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (SecurityException e) {
            String message = String.format(
                    "The method %1$s.%2$s is probably private or protected and cannot be mocked.", declaringClass, methodName);
            throw new MockitoException(message, e);
        } catch (NoSuchMethodException e) {
            String message = String.format(
                    "The method %1$s.%2$s does not exists and you should not get to this point.\n" +
                            "Please report this as a defect with an example of how to reproduce it.", declaringClass, methodName);
            throw new MockitoException(message, e);
        }
    }

    public String getName() {
        return methodName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

}
