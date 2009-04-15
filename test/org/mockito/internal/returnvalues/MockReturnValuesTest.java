package org.mockito.internal.returnvalues;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

public class MockReturnValuesTest extends TestBase {
    private MockReturnValues values = new MockReturnValues();

    interface FooInterface {
    }

    class BarClass {
    }

    final class Baz {

    }

    @Test
    // FIXME split into separate
    public void shouldReturnMockValueForInterface() throws Exception {
        Object interfaceMock = values.returnValueFor(FooInterface.class);
        assertTrue(MockUtil.isMock(interfaceMock));
    }

    public void shouldReturnMockValueForClass() throws Exception {
        Object classMock = values.returnValueFor(BarClass.class);
        assertTrue(MockUtil.isMock(classMock));
    }

    @Test
    public void shouldReturnNullForFinalClass() throws Exception {
        assertNull(values.returnValueFor(Baz.class));
    }

    private Invocation invocationOf(Class<?> type, String methodName)
            throws NoSuchMethodException {
        return new Invocation(new Object(), type.getMethod(methodName,
                new Class[0]), new Object[0], 1);
    }

    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives()
            throws Exception {
        MockReturnValues returnValues = new MockReturnValues();
        assertEquals(false, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0, returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface StringMethods {
        String stringMethod();
        String[] stringArrayMethod();
    }
    
    @Test
    public void shouldReturnEmptyArray() throws Exception{
        String[] ret = (String[]) values.valueFor(invocationOf(StringMethods.class, "stringArrayMethod"));
        
        assertTrue(ret.getClass().isArray());
        assertTrue(ret.length == 0);
    }
    
    @Test
    public void shouldReturnEmptyString() throws Exception{
        assertEquals("", values.valueFor(invocationOf(StringMethods.class, "stringMethod")));
    }
}
