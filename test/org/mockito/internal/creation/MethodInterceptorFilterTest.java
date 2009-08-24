package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class MethodInterceptorFilterTest extends TestBase {

    IMockHandler mockHanlder = Mockito.mock(IMockHandler.class);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHanlder);

    @Before
    public void setUp() {
        filter.cglibHacker = Mockito.mock(CGLIBHacker.class);        
    }

    @Test
    public void shouldBeSerializable() throws Exception {
        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(new MethodInterceptorFilter(null));
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
}