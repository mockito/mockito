/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import org.mockito.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Field;

class CGLIBHacker {

    public void setMockitoNamingPolicy(MethodProxy methodProxy) {
        try {
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