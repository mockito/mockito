package org.mockito.internal.invocation;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class SerializableMockitoMethod implements MockitoMethod, Serializable {

    private static final long serialVersionUID = 6005610965006048445L;
    private Class<?> declaringClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;
    private Class<?>[] exceptionTypes;
    private boolean isVarArgs;

    public SerializableMockitoMethod(Method method) {
        declaringClass = method.getDeclaringClass();
        methodName = method.getName();
        parameterTypes = method.getParameterTypes();
        returnType = method.getReturnType();
        exceptionTypes = method.getExceptionTypes();
        isVarArgs = method.isVarArgs();
    }

    public String getName() {
      return methodName;
    }
    
    public Class<?> getReturnType() {
      return returnType;
    }
    
    public Class<?>[] getParameterTypes() {
      return parameterTypes;
    }

    public Class<?>[] getExceptionTypes() {
      return exceptionTypes;
    }

    public boolean isVarArgs() {
      return isVarArgs;
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
        SerializableMockitoMethod other = (SerializableMockitoMethod) obj;
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
}
