/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.mockito.internal.creation.MockitoMethodProxy;
import org.mockito.cglib.proxy.MethodProxy;

public class CGLIBHacker implements Serializable {

    private static final long serialVersionUID = -4389233991416356668L;

    public void setMockitoNamingPolicy(MockitoMethodProxy mockitoMethodProxy) {
        try {
            MethodProxy methodProxy = mockitoMethodProxy.getMethodProxy();
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
}