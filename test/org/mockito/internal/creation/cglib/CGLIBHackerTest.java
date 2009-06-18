/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.core.NamingPolicy;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockitoutil.TestBase;
import org.powermock.reflect.Whitebox;

public class CGLIBHackerTest extends TestBase {

    @Test
    public void shouldSetMockitoNamingPolicy() throws Exception {
        //given
        MethodProxy methodProxy = new MethodProxyBuilder().build();
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        //then
        Object createInfo = Whitebox.getInternalState(methodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(namingPolicy, MockitoNamingPolicy.INSTANCE);
    }
    
    @Test
    public void shouldSetMockitoNamingPolicyEvenIfMethodProxyIsProxied() throws Exception {
        //given
        MethodProxy proxiedMethodProxy = spy(new MethodProxyBuilder().build());
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(proxiedMethodProxy);
        
        //then
        Object createInfo = Whitebox.getInternalState(proxiedMethodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(namingPolicy, MockitoNamingPolicy.INSTANCE);
    }
}