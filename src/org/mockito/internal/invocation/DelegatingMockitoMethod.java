package org.mockito.internal.invocation;

import java.lang.reflect.Method;

public class DelegatingMockitoMethod implements MockitoMethod {

    private final Method method;

    public DelegatingMockitoMethod(Method method) {
        this.method = method;
    }

    public Class<?>[] getExceptionTypes() {
        return method.getExceptionTypes();
    }

    public String getName() {
        return method.getName();
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
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
        DelegatingMockitoMethod other = (DelegatingMockitoMethod) obj;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        return true;
    }

}
