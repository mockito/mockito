/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import org.junit.Test;
import org.mockito.cglib.core.NamingPolicy;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockitoutil.TestBase;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.spy;

public class CGLIBHackerTest extends TestBase {

    @Test
    public void shouldSetMockitoNamingPolicy() throws Exception {
        //given
        MethodProxy methodProxy = MethodProxy.create(String.class, Integer.class, "", "", "");
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        //then
        Object createInfo = Whitebox.getInternalState(methodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(MockitoNamingPolicy.INSTANCE, namingPolicy);
    }
    
    @Test
    public void shouldSetMockitoNamingPolicyEvenIfMethodProxyIsProxied() throws Exception {
        //given
        MethodProxy proxiedMethodProxy = spy(MethodProxy.create(String.class, Integer.class, "", "", ""));
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(proxiedMethodProxy);
        
        //then
        Object createInfo = Whitebox.getInternalState(proxiedMethodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(MockitoNamingPolicy.INSTANCE, namingPolicy);
    }
}