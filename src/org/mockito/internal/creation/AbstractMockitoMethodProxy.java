package org.mockito.internal.creation;

import java.lang.reflect.Field;

import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.cglib.MockitoNamingPolicy;

public abstract class AbstractMockitoMethodProxy implements MockitoMethodProxy {

    public Object invokeSuper(Object target, Object[] arguments) throws Throwable {
        return getMethodProxy().invokeSuper(target, arguments);
    }

    //TODO refactor back to CGLIBHacker and get rid of abstract class
    public void setNamingPolicyField(MockitoNamingPolicy namingPolicy) {
        try {
            MethodProxy methodProxy = getMethodProxy();
            Field createInfoField = reflectOnCreateInfo(methodProxy);
            createInfoField.setAccessible(true);
            Object createInfo = createInfoField.get(methodProxy);
            Field namingPolicyField = createInfo.getClass().getDeclaredField("namingPolicy");
            namingPolicyField.setAccessible(true);
            if (namingPolicyField.get(createInfo) == null) {
                namingPolicyField.set(createInfo, MockitoNamingPolicy.INSTANCE);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                            "Unable to set MockitoNamingPolicy on cglib generator which creates FastClasses", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Field reflectOnCreateInfo(MethodProxy methodProxy) throws SecurityException, NoSuchFieldException {
        
        Class cglibMethodProxyClass = methodProxy.getClass();
        // in case methodProxy was extended by user, let's traverse the object
        // graph to find the cglib methodProxy
        // with all the fields we would like to change
        while (cglibMethodProxyClass != MethodProxy.class) {
            cglibMethodProxyClass = methodProxy.getClass().getSuperclass();
        }
        return cglibMethodProxyClass.getDeclaredField("createInfo");
    }


    protected abstract MethodProxy getMethodProxy();
}
