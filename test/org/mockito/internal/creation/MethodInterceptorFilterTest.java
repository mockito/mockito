package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import org.mockito.internal.IMockHandler;
import org.mockito.internal.creation.cglib.CGLIBHacker;
import org.mockito.internal.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class MethodInterceptorFilterTest extends TestBase {

    IMockHandler mockHanlder = Mockito.mock(IMockHandler.class);
    MethodInterceptorFilter filter = new MethodInterceptorFilter(IMethods.class, mockHanlder);

    @Before
    public void setUp() {
        filter.cglibHacker = Mockito.mock(CGLIBHacker.class);        
    }

    @Ignore
    @Test
    public void shouldBeSerializable() throws Exception {
        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(filter);
    }

    @Test
    public void shouldProvideOwnImplementationOfhashCode() throws Throwable {
        //given
        Object ret = filter.intercept(new MethodsImpl(), MethodsImpl.class.getMethod("hashCode"), new Object[0], null);

        //then
        assertTrue((Integer) ret != 0);
        Mockito.verify(mockHanlder, never()).handle(any(Invocation.class));
    }
}