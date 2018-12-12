/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockitoutil.TestBase;

import javax.net.SocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


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

        public Street getStreet(Locale locale) {
            return street;
        }
    }

    static class Street {
        String name;

        public String getName() {
            return name;
        }

        public String getLongName() {
            return name;
        }
    }

    static final class FinalClass {}

    interface First {
        Second getSecond();

        String getString();
    }

    interface Second extends List<String> {}

    class BaseClassGenerics<A, B> {}

    class ReversedGenerics<A, B> extends BaseClassGenerics<A, B> {
        ReversedGenerics<B, A> reverse() {
            return null;
        }

        A finalMethod() {
            return null;
        }
    }

    class SuperOfReversedGenerics extends ReversedGenerics<String, Long> {}

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
    public void verification_work_with_argument_Matchers_in_nested_calls() throws Exception {
        //given
        person.getAddress("111 Mock Lane").getStreet();
        person.getAddress("111 Mock Lane").getStreet(Locale.ITALIAN).getName();

        //then
        verify(person.getAddress(anyString())).getStreet();
        verify(person.getAddress(anyString()).getStreet(Locale.CHINESE), never()).getName();
        verify(person.getAddress(anyString()).getStreet(eq(Locale.ITALIAN))).getName();
    }

    @Test
    public void deep_stub_return_same_mock_instance_if_invocation_matchers_matches() throws Exception {
        when(person.getAddress(anyString()).getStreet().getName()).thenReturn("deep");

        person.getAddress("the docks").getStreet().getName();

        assertSame(person.getAddress("the docks").getStreet(), person.getAddress(anyString()).getStreet());
        assertSame(person.getAddress(anyString()).getStreet(), person.getAddress(anyString()).getStreet());
        assertSame(person.getAddress("the docks").getStreet(), person.getAddress("the docks").getStreet());
        assertSame(person.getAddress(anyString()).getStreet(), person.getAddress("the docks").getStreet());
        assertSame(person.getAddress("111 Mock Lane").getStreet(), person.getAddress("the docks").getStreet());
    }

    @Test
    public void times_never_atLeast_atMost_verificationModes_should_work() throws Exception {
        when(person.getAddress(anyString()).getStreet().getName()).thenReturn("deep");

        person.getAddress("the docks").getStreet().getName();
        person.getAddress("the docks").getStreet().getName();
        person.getAddress("the docks").getStreet().getName();
        person.getAddress("the docks").getStreet(Locale.ITALIAN).getName();

        verify(person.getAddress("the docks").getStreet(), times(3)).getName();
        verify(person.getAddress("the docks").getStreet(Locale.CHINESE), never()).getName();
        verify(person.getAddress("the docks").getStreet(Locale.ITALIAN), atMost(1)).getName();
    }


    @Test
    public void inOrder_only_work_on_the_very_last_mock_but_it_works() throws Exception {
        when(person.getAddress(anyString()).getStreet().getName()).thenReturn("deep");
        when(person.getAddress(anyString()).getStreet(Locale.ITALIAN).getName()).thenReturn("deep");
        when(person.getAddress(anyString()).getStreet(Locale.CHINESE).getName()).thenReturn("deep");

        person.getAddress("the docks").getStreet().getName();
        person.getAddress("the docks").getStreet().getLongName();
        person.getAddress("the docks").getStreet(Locale.ITALIAN).getName();
        person.getAddress("the docks").getStreet(Locale.CHINESE).getName();

        InOrder inOrder = inOrder(
                person.getAddress("the docks").getStreet(),
                person.getAddress("the docks").getStreet(Locale.CHINESE),
                person.getAddress("the docks").getStreet(Locale.ITALIAN)
        );
        inOrder.verify(person.getAddress("the docks").getStreet(), times(1)).getName();
        inOrder.verify(person.getAddress("the docks").getStreet()).getLongName();
        inOrder.verify(person.getAddress("the docks").getStreet(Locale.ITALIAN), atLeast(1)).getName();
        inOrder.verify(person.getAddress("the docks").getStreet(Locale.CHINESE)).getName();
    }

    @Test
    public void verificationMode_only_work_on_the_last_returned_mock() throws Exception {
        // 1st invocation on Address mock (stubbing)
        when(person.getAddress("the docks").getStreet().getName()).thenReturn("deep");

        // 2nd invocation on Address mock (real)
        person.getAddress("the docks").getStreet().getName();
        // 3rd invocation on Address mock (verification)
        // (Address mock is not in verification mode)
        verify(person.getAddress("the docks").getStreet()).getName();

        try {
            verify(person.getAddress("the docks"), times(1)).getStreet();
            fail();
        } catch (TooManyActualInvocations e) {
            assertThat(e.getMessage())
                    .contains("Wanted 1 time")
                    .contains("But was 3 times");
        }
    }

    @Test
    public void shouldFailGracefullyWhenClassIsFinal() throws Exception {
        //when
        FinalClass value = new FinalClass();
        given(person.getFinalClass()).willReturn(value);

        //then
        assertEquals(value, person.getFinalClass());
    }

    @Test
    public void deep_stub_does_not_try_to_mock_generic_final_classes() {
        First first = mock(First.class, RETURNS_DEEP_STUBS);
        assertNull(first.getString());
        assertNull(first.getSecond().get(0));
    }

    @Test
    public void deep_stub_does_not_stack_overflow_on_reversed_generics() {
        SuperOfReversedGenerics mock = mock(SuperOfReversedGenerics.class, RETURNS_DEEP_STUBS);

        when((Object) mock.reverse().finalMethod()).thenReturn(5L);

        assertThat(mock.reverse().finalMethod()).isEqualTo(5L);
    }

}
