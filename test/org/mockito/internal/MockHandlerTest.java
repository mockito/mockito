/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.creation.*;
import org.mockito.internal.invocation.*;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;
@SuppressWarnings({"unchecked","serial"})
public class MockHandlerTest extends TestBase {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        //given
        Invocation invocation = new InvocationBuilder().toInvocation();
        MockHandler handler = new MockHandler();
        handler.mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
        handler.matchersBinder = new MatchersBinder() {
            public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
                throw new InvalidUseOfMatchersException();
            }
        };
        
        try {
            //when
            handler.handle(invocation);
            
            //then
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(handler.mockingProgress.pullVerificationMode());
    }
    
    @Test
    public void shouldCreateSerializableMethodProxyIfIsSerializableMock() throws Exception {
        MockSettingsImpl mockSettings = (MockSettingsImpl) withSettings().serializable();
        MockHandler handler = new MockHandler(null, null, null, mockSettings);
        MethodProxy methodProxy = MethodProxy.create(String.class, String.class, "", "toString", "toString");
        
        // when
        MockitoMethodProxy mockitoMethodProxy = handler.createMockitoMethodProxy(methodProxy);
        
        // then
        assertThat(mockitoMethodProxy, instanceOf(SerializableMockitoMethodProxy.class));
    }
    
    @Test
    public void shouldCreateNONSerializableMethodProxyIfIsNotSerializableMock() throws Exception {
        MockHandler handler = new MockHandler(null, null, null, (MockSettingsImpl) withSettings());
        MethodProxy methodProxy = MethodProxy.create(String.class, String.class, "", "toString", "toString");
        
        // when
        MockitoMethodProxy mockitoMethodProxy = handler.createMockitoMethodProxy(methodProxy);
        
        // then
        assertThat(mockitoMethodProxy, instanceOf(DelegatingMockitoMethodProxy.class));
    }
    
    @Test
    public void shouldCreateSerializableMethodIfIsSerializableMock() throws Exception {
        // given
        MockSettingsImpl mockSettings = (MockSettingsImpl) withSettings().serializable();
        MockHandler handler = new MockHandler(null, null, null, mockSettings);
        Method method = getClass().getMethod("toString", new Class<?>[0]);
        
        // when
        MockitoMethod mockitoMethod = handler.createMockitoMethod(method);
        
        // then
        assertThat(mockitoMethod, instanceOf(SerializableMockitoMethod.class));
    }
    
    @Test
    public void shouldCreateNONSerializableMethodIfIsNotSerializableMock() throws Exception {
        // given
        MockSettingsImpl mockSettings = (MockSettingsImpl) withSettings();
        MockHandler handler = new MockHandler(null, null, null, mockSettings);
        Method method = getClass().getMethod("toString", new Class<?>[0]);
        
        // when
        MockitoMethod mockitoMethod = handler.createMockitoMethod(method);
        
        // then
        assertThat(mockitoMethod, instanceOf(DelegatingMockitoMethod.class));
    }
}