package org.mockito.internal.invocation;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockitoutil.TestBase;


public class SerializableMockitoMethodTest extends TestBase {

    private MockitoMethod mockMethod;
    private Method toStringMethod;
    private Class<?>[] args;

    @Before
    public void createMethodToTestWith() throws SecurityException, NoSuchMethodException {
        args = new Class<?>[0];
        toStringMethod = this.getClass().getMethod("toString", args);
        mockMethod = new MockitoMethod(toStringMethod);
    }
    
    @Test
    public void shouldBeSerializable() throws Exception {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        new ObjectOutputStream(serialized).writeObject(mockMethod);
    }
    
    @Test
    public void shouldBeAbleToRetrieveMethodExceptionTypes() throws Exception {
        assertArrayEquals(toStringMethod.getExceptionTypes(), mockMethod.getExceptionTypes());
    }
    
    @Test
    public void shouldBeAbleToRetrieveMethodName() throws Exception {
        assertEquals(toStringMethod.getName(), mockMethod.getName());
    }
    
    @Test
    public void shouldBeAbleToCheckIsArgVargs() throws Exception {
        assertEquals(toStringMethod.isVarArgs(), mockMethod.isVarArgs());
    }
    
    @Test
    public void shouldBeAbleToGetParameterTypes() throws Exception {
        assertArrayEquals(toStringMethod.getParameterTypes(), mockMethod.getParameterTypes());
    }
    
    @Test
    public void shouldBeAbleToGetReturnType() throws Exception {
        assertEquals(toStringMethod.getReturnType(), mockMethod.getReturnType());
    }
    
    @Test
    public void shouldBeEqualForTwoInstances() throws Exception {
        assertTrue(new MockitoMethod(toStringMethod).equals(mockMethod));
    }
    
    @Test
    public void shouldNotBeEqualForSameMethodFromTwoDifferentClasses() throws Exception {
        Method testBaseToStringMethod = String.class.getMethod("toString", args);
        assertFalse(new MockitoMethod(testBaseToStringMethod).equals(mockMethod));
    }
    
    //TODO: add tests for generated equals() method
    
}