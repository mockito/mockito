/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "serial"})
public class MocksSerializationTest extends TestBase implements Serializable {

    private static final long serialVersionUID = 6160482220413048624L;

    @Test
    public void shouldAllowThrowsExceptionToBeSerializable() throws Exception {
        // given
        Bar mock = mock(Bar.class, new ThrowsException(new RuntimeException()));
        // when-serialize then-deserialize
        serializeAndBack(mock);
    }

    @Test
    public void shouldAllowMethodDelegation() throws Exception {
        // given
        Bar barMock = mock(Bar.class, withSettings().serializable());
        Foo fooMock = mock(Foo.class);
        when(barMock.doSomething()).thenAnswer(new ThrowsException(new RuntimeException()));

        //when-serialize then-deserialize
        serializeAndBack(barMock);
    }
    
    @Test
    public void shouldAllowMockToBeSerializable() throws Exception {
        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());

        // when-serialize then-deserialize
        serializeAndBack(mock);
    }

    @Test
    public void shouldAllowMockAndBooleanValueToSerializable() throws Exception {
        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        when(mock.booleanReturningMethod()).thenReturn(true);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertTrue(readObject.booleanReturningMethod());
    }

    @Test
    public void shouldAllowMockAndStringValueToBeSerializable() throws Exception {
        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        String value = "value";
        when(mock.stringReturningMethod()).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.stringReturningMethod());
    }

    @Test
    public void shouldAllMockAndSerializableValueToBeSerialized() throws Exception {
        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        List<?> value = Collections.emptyList();
        when(mock.objectReturningMethodNoArgs()).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectReturningMethodNoArgs());
    }

    @Test
    public void shouldSerializeMethodCallWithParametersThatAreSerializable() throws Exception {
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        List<?> value = Collections.emptyList();
        when(mock.objectArgMethod(value)).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectArgMethod(value));
    }

    @Test
    public void shouldSerializeMethodCallsUsingAnyStringMatcher() throws Exception {
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        List<?> value = Collections.emptyList();
        when(mock.objectArgMethod(anyString())).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectArgMethod(""));
    }

    @Test
    public void shouldVerifyCalledNTimesForSerializedMock() throws Exception {
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        List<?> value = Collections.emptyList();
        when(mock.objectArgMethod(anyString())).thenReturn(value);
        mock.objectArgMethod("");

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        verify(readObject, times(1)).objectArgMethod("");
    }

    @Test
    public void shouldVerifyEvenIfSomeMethodsCalledAfterSerialization() throws Exception {
        //given
        IMethods mock = mock(IMethods.class, withSettings().serializable());

        // when
        mock.simpleMethod(1);
        ByteArrayOutputStream serialized = serializeMock(mock);
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        readObject.simpleMethod(1);

        // then
        verify(readObject, times(2)).simpleMethod(1);

        //this test is working because it seems that java serialization mechanism replaces all instances
        //of serialized object in the object graph (if there are any)
    }

    class Bar implements Serializable {
        Foo foo;

        public Foo doSomething() {
            return foo;
        }
    }

    class Foo implements Serializable {
        Bar bar;
        Foo() {
            bar = new Bar();
            bar.foo = this;
        }
    }

    @Test
    public void shouldSerializationWork() throws Exception {
        //given
        Foo foo = new Foo();
        //when
        foo = serializeAndBack(foo);
        //then
        assertSame(foo, foo.bar.foo);
    }

    @Test
    public void shouldStubEvenIfSomeMethodsCalledAfterSerialization() throws Exception {
        //given
        IMethods mock = mock(IMethods.class, withSettings().serializable());

        // when
        when(mock.simpleMethod(1)).thenReturn("foo");
        ByteArrayOutputStream serialized = serializeMock(mock);
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        when(readObject.simpleMethod(2)).thenReturn("bar");

        // then
        assertEquals("foo", readObject.simpleMethod(1));
        assertEquals("bar", readObject.simpleMethod(2));
    }

    @Test
    public void shouldVerifyCallOrderForSerializedMock() throws Exception {
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        IMethods mock2 = mock(IMethods.class, withSettings().serializable());
        mock.arrayReturningMethod();
        mock2.arrayReturningMethod();

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);
        ByteArrayOutputStream serialized2 = serializeMock(mock2);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        IMethods readObject2 = deserializeMock(serialized2, IMethods.class);
        InOrder inOrder = inOrder(readObject, readObject2);
        inOrder.verify(readObject).arrayReturningMethod();
        inOrder.verify(readObject2).arrayReturningMethod();
    }

    @Test
    public void shouldRememberInteractionsForSerializedMock() throws Exception {
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        List<?> value = Collections.emptyList();
        when(mock.objectArgMethod(anyString())).thenReturn(value);
        mock.objectArgMethod("happened");

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        verify(readObject, never()).objectArgMethod("never happened");
    }

    @Test
    public void shouldSerializeWithStubbingCallback() throws Exception {

        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());
        CustomAnswersMustImplementSerializableForSerializationToWork answer = 
            new CustomAnswersMustImplementSerializableForSerializationToWork();
        answer.string = "return value";
        when(mock.objectArgMethod(anyString())).thenAnswer(answer);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(answer.string, readObject.objectArgMethod(""));
    }

    class CustomAnswersMustImplementSerializableForSerializationToWork 
        implements Answer<Object>, Serializable {
        private String string;
        public Object answer(InvocationOnMock invocation) throws Throwable {
            invocation.getArguments();
            invocation.getMock();
            return string;
        }
    }
  
    @Test
    public void shouldSerializeWithRealObjectSpy() throws Exception {
        // given
        List<Object> list = new ArrayList<Object>();
        List<Object> spy = mock(ArrayList.class, withSettings()
                        .spiedInstance(list)
                        .defaultAnswer(CALLS_REAL_METHODS)
                        .serializable());
        when(spy.size()).thenReturn(100);

        // when
        ByteArrayOutputStream serialized = serializeMock(spy);

        // then
        List<?> readObject = deserializeMock(serialized, List.class);
        assertEquals(100, readObject.size());
    }

    @Test
    public void shouldSerializeObjectMock() throws Exception {
        // given
        Any mock = mock(Any.class);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        deserializeMock(serialized, Any.class);
    }

    @Test
    public void shouldSerializeRealPartialMock() throws Exception {
        // given
        Any mock = mock(Any.class, withSettings().serializable());
        when(mock.matches(anyObject())).thenCallRealMethod();

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        Any readObject = deserializeMock(serialized, Any.class);
        readObject.matches("");
    }

    class AlreadySerializable implements Serializable {}

    @Test
    public void shouldSerializeAlreadySerializableClass() throws Exception {
        // given
        AlreadySerializable mock = mock(AlreadySerializable.class, withSettings().serializable());
        when(mock.toString()).thenReturn("foo");

        // when
        mock = serializeAndBack(mock);

        // then
        assertEquals("foo", mock.toString());
    }

    @Test
    public void shouldBeSerializeAndHaveExtraInterfaces() throws Exception {
        //when
        IMethods mock = mock(IMethods.class, withSettings().serializable().extraInterfaces(List.class));
        IMethods mockTwo = mock(IMethods.class, withSettings().extraInterfaces(List.class).serializable());

        //then
        serializeAndBack((List) mock);
        serializeAndBack((List) mockTwo);
    }
}