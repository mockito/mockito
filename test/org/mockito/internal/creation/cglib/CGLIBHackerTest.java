/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.cglib;

import org.junit.Test;
import org.mockito.cglib.core.NamingPolicy;
import org.mockitoutil.TestBase;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.spy;

public class CGLIBHackerTest extends TestBase {

    @Test
    public void shouldSetMockitoNamingPolicy() throws Exception {
        //given
        MockitoMethodProxy methodProxy = new MethodProxyBuilder().build();
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(methodProxy);
        
        //then
        Object realMethodProxy = Whitebox.invokeMethod(methodProxy, "getMethodProxy", new Object[0]);
        Object createInfo = Whitebox.getInternalState(realMethodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(MockitoNamingPolicy.INSTANCE, namingPolicy);
    }
    
    @Test
    public void shouldSetMockitoNamingPolicyEvenIfMethodProxyIsProxied() throws Exception {
        //given
        MockitoMethodProxy proxiedMethodProxy = spy(new MethodProxyBuilder().build());
        
        //when
        new CGLIBHacker().setMockitoNamingPolicy(proxiedMethodProxy);
        
        //then
        Object realMethodProxy = Whitebox.invokeMethod(proxiedMethodProxy, "getMethodProxy", new Object[0]);
        Object createInfo = Whitebox.getInternalState(realMethodProxy, "createInfo");
        NamingPolicy namingPolicy = (NamingPolicy) Whitebox.getInternalState(createInfo, "namingPolicy");
        assertEquals(MockitoNamingPolicy.INSTANCE, namingPolicy);
    }
}