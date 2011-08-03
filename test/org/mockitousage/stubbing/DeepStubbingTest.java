/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.SocketFactory;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class DeepStubbingTest extends TestBase {

    static class Person {
        Address address;

        public Address getAddress() {
            return address;
        }
        
        public Address getAddress(String addressName) {
        	return address;
        }
        
        public FinalClass getFinalClass() {
            return null;
        }
    }
    
    static class Address {
        Street street;

        public Street getStreet() {
            return street;
        }
    }
    
    static class Street {
        String name;

        public String getName() {
            return name;
        }
    }    
    
    static final class FinalClass {};    
    
    @Test
    public void myTest() throws Exception {
        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket(anyString(), eq(80))).thenReturn(null);
        sf.createSocket("what", 80);
    }

    @Test
    public void simpleCase() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(out);

        assertSame(out, socket.getOutputStream());
    }

    /**
     * Test that deep stubbing works for one intermediate level
     */
    @Test
    public void oneLevelDeep() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        SocketFactory socketFactory = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(socketFactory.createSocket().getOutputStream()).thenReturn(out);

        assertSame(out, socketFactory.createSocket().getOutputStream());
    }

    /**
     * Test that stubbing of two mocks stubs don't interfere
     */
    @Test
    public void interactions() throws Exception {
        OutputStream out1 = new ByteArrayOutputStream();
        OutputStream out2 = new ByteArrayOutputStream();

        SocketFactory sf1 = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf1.createSocket().getOutputStream()).thenReturn(out1);

        SocketFactory sf2 = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf2.createSocket().getOutputStream()).thenReturn(out2);

        assertSame(out1, sf1.createSocket().getOutputStream());
        assertSame(out2, sf2.createSocket().getOutputStream());
    }

    /**
     * Test that stubbing of methods of different arguments don't interfere
     */
    @Test
    public void withArguments() throws Exception {
        OutputStream out1 = new ByteArrayOutputStream();
        OutputStream out2 = new ByteArrayOutputStream();
        OutputStream out3 = new ByteArrayOutputStream();

        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket().getOutputStream()).thenReturn(out1);
        when(sf.createSocket("google.com", 80).getOutputStream()).thenReturn(out2);
        when(sf.createSocket("stackoverflow.com", 80).getOutputStream()).thenReturn(out3);

        assertSame(out1, sf.createSocket().getOutputStream());
        assertSame(out2, sf.createSocket("google.com", 80).getOutputStream());
        assertSame(out3, sf.createSocket("stackoverflow.com", 80).getOutputStream());
    }

    /**
     * Test that deep stubbing work with argument patterns
     */
    @Test
    public void withAnyPatternArguments() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        //TODO: should not use javax in case it changes
        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket(anyString(), anyInt()).getOutputStream()).thenReturn(out);

        assertSame(out, sf.createSocket("google.com", 80).getOutputStream());
        assertSame(out, sf.createSocket("stackoverflow.com", 8080).getOutputStream());
    }

    /**
     * Test that deep stubbing work with argument patterns
     */
    @Test
    public void withComplexPatternArguments() throws Exception {
        OutputStream out1 = new ByteArrayOutputStream();
        OutputStream out2 = new ByteArrayOutputStream();

        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket(anyString(), eq(80)).getOutputStream()).thenReturn(out1);
        when(sf.createSocket(anyString(), eq(8080)).getOutputStream()).thenReturn(out2);

        assertSame(out2, sf.createSocket("stackoverflow.com", 8080).getOutputStream());
        assertSame(out1, sf.createSocket("google.com", 80).getOutputStream());
        assertSame(out2, sf.createSocket("google.com", 8080).getOutputStream());
        assertSame(out1, sf.createSocket("stackoverflow.com", 80).getOutputStream());
    }

    /**
     * Test that deep stubbing work with primitive expected values
     */
    @Test
    public void withSimplePrimitive() throws Exception {
        int a = 32;

        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket().getPort()).thenReturn(a);

        assertEquals(a, sf.createSocket().getPort());
    }

    /**
     * Test that deep stubbing work with primitive expected values with
     * pattern method arguments
     */
    @Test
    public void withPatternPrimitive() throws Exception {
        int a = 12, b = 23, c = 34;

        SocketFactory sf = mock(SocketFactory.class, RETURNS_DEEP_STUBS);
        when(sf.createSocket(eq("stackoverflow.com"), eq(80)).getPort()).thenReturn(a);
        when(sf.createSocket(eq("google.com"), anyInt()).getPort()).thenReturn(b);
        when(sf.createSocket(eq("stackoverflow.com"), eq(8080)).getPort()).thenReturn(c);

        assertEquals(b, sf.createSocket("google.com", 80).getPort());
        assertEquals(c, sf.createSocket("stackoverflow.com", 8080).getPort());
        assertEquals(a, sf.createSocket("stackoverflow.com", 80).getPort());
    }
    
    Person person = mock(Person.class, RETURNS_DEEP_STUBS);
       
    @Test
    public void shouldStubbingBasicallyWorkFine() throws Exception {
        //given
        given(person.getAddress().getStreet().getName()).willReturn("Norymberska");
        
        //when
        String street = person.getAddress().getStreet().getName();
        
        //then
        assertEquals("Norymberska", street);
    }    
    
    @Test
    public void shouldVerificationBasicallyWorkFine() throws Exception {
        //given
        person.getAddress().getStreet().getName();
        
        //then
        verify(person.getAddress().getStreet()).getName();
    }
    
    @Test
	public void shouldVerificationWorkWithArgumentMatchersInNestedCalls() throws Exception {
		//given
    	person.getAddress("111 Mock Lane").getStreet();
		
		//then
    	verify(person.getAddress(anyString())).getStreet();
	}
    
    @Test
    public void shouldFailGracefullyWhenClassIsFinal() throws Exception {
        //when        
        FinalClass value = new FinalClass();
        given(person.getFinalClass()).willReturn(value);
        
        //then
        assertEquals(value, person.getFinalClass());
    }
}