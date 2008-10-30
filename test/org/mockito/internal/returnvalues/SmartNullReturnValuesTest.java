package org.mockito.internal.returnvalues;

import java.util.List;

import org.junit.Test;
import org.mockito.configuration.ReturnValues;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.internal.invocation.Invocation;
import org.mockitoutil.TestBase;

public class SmartNullReturnValuesTest extends TestBase {
    
    interface HasPrimitiveMethods {
        boolean booleanMethod();
        char charMethod();
        int intMethod();
        long longMethod();
        float floatMethod();
        double doubleMethod();
    }

    private Invocation invocationOf(Class<?> type, String methodName) throws NoSuchMethodException {
        return new Invocation(new Object(), type.getMethod(methodName, new Class[0]), new Object[0], 1);
    }
    
    //TODO review other default return values
    @Test
    public void shouldReturnTheUsualDefaultValuesForPrimitives() throws Exception {
        SmartNullReturnValues returnValues = new SmartNullReturnValues();
        assertEquals(false  ,   returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "booleanMethod")));
        assertEquals((char) 0,  returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "charMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "intMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "longMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "floatMethod")));
        assertEquals(0,         returnValues.valueFor(invocationOf(HasPrimitiveMethods.class, "doubleMethod")));
    }
    
    interface HasInterfaceMethod {
        List<String> doListStuff();
    }
    
    @Test
    public void shouldReturnAnObjectThatFailsOnAnyMethodInvocationForNonPrimitives() throws Exception {
        ReturnValues returnValues = new SmartNullReturnValues();
        
        @SuppressWarnings("unchecked")
        List<String> smartNull = (List<String>) returnValues.valueFor(invocationOf(HasInterfaceMethod.class, "doListStuff"));
        
        try {
            smartNull.iterator();
            fail();
        } catch (SmartNullPointerException expected) {}
    }
}
