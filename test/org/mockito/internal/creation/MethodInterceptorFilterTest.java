package org.mockito.internal.creation;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.withSettings;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

public class MethodInterceptorFilterTest extends TestBase {

    IMockHandler mockHanlder = Mockito.mock(IMockHandler.class);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHanlder, (MockSettingsImpl) withSettings());

    @Before
    public void setUp() {
        filter.cglibHacker = Mockito.mock(CGLIBHacker.class);        
    }

    @Test
    public void shouldBeSerializable() throws Exception {
        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(new MethodInterceptorFilter(null, null));
    }

    @Test
    public void shouldProvideOwnImplementationOfHashCode() throws Throwable {
        //when
        Object ret = filter.intercept(new MethodsImpl(), MethodsImpl.class.getMethod("hashCode"), new Object[0], null);

        //then
        assertTrue((Integer) ret != 0);
        Mockito.verify(mockHanlder, never()).handle(any(Invocation.class));
    }

    @Test
    public void shouldProvideOwnImplementationOfEquals() throws Throwable {
        //when
        MethodsImpl proxy = new MethodsImpl();
        Object ret = filter.intercept(proxy, MethodsImpl.class.getMethod("equals", Object.class), new Object[] {proxy}, null);

        //then
        assertTrue((Boolean) ret);
        Mockito.verify(mockHanlder, never()).handle(any(Invocation.class));
    }
    
    //TODO: move to separate factory
    @Test
    public void shouldCreateSerializableMethodProxyIfIsSerializableMock() throws Exception {
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHanlder, (MockSettingsImpl) withSettings().serializable());
        MethodProxy methodProxy = MethodProxy.create(String.class, String.class, "", "toString", "toString");
        
        // when
        MockitoMethodProxy mockitoMethodProxy = filter.createMockitoMethodProxy(methodProxy);
        
        // then
        assertThat(mockitoMethodProxy, instanceOf(SerializableMockitoMethodProxy.class));
    }
    
    @Test
    public void shouldCreateNONSerializableMethodProxyIfIsNotSerializableMock() throws Exception {
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHanlder, (MockSettingsImpl) withSettings());
        MethodProxy methodProxy = MethodProxy.create(String.class, String.class, "", "toString", "toString");
        
        // when
        MockitoMethodProxy mockitoMethodProxy = filter.createMockitoMethodProxy(methodProxy);
        
        // then
        assertThat(mockitoMethodProxy, instanceOf(DelegatingMockitoMethodProxy.class));
    }
}