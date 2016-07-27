/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;
import static org.mockitoutil.SimpleSerializationUtil.*;

@SuppressWarnings({"unchecked", "serial"})
public class MocksSerializationForAnnotationTest extends TestBase implements Serializable {

    private static final long serialVersionUID = 6160482220413048624L;

    @Mock Any any;
    @Mock(serializable=true) Bar barMock;
    @Mock(serializable=true) IMethods imethodsMock;
    @Mock(serializable=true) IMethods imethodsMock2;
    @Mock(serializable=true) Any anyMock;
    @Mock(serializable=true) AlreadySerializable alreadySerializableMock;
    @Mock(extraInterfaces={List.class},serializable=true) IMethods imethodsWithExtraInterfacesMock;
    
    @Test
    public void should_allow_throws_exception_to_be_serializable() throws Exception {
        // given
        when(barMock.doSomething()).thenAnswer(new ThrowsException(new RuntimeException()));

        //when-serialize then-deserialize
        serializeAndBack(barMock);
    }
    
    @Test
    public void should_allow_mock_to_be_serializable() throws Exception {
        // when-serialize then-deserialize
        serializeAndBack(imethodsMock);
    }

    @Test
    public void should_allow_mock_and_boolean_value_to_serializable() throws Exception {
        // given
        when(imethodsMock.booleanReturningMethod()).thenReturn(true);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertTrue(readObject.booleanReturningMethod());
    }

    @Test
    public void should_allow_mock_and_string_value_to_be_serializable() throws Exception {
        // given
        String value = "value";
        when(imethodsMock.stringReturningMethod()).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.stringReturningMethod());
    }

    @Test
    public void should_all_mock_and_serializable_value_to_be_serialized() throws Exception {
        // given
        List<?> value = Collections.emptyList();
        when(imethodsMock.objectReturningMethodNoArgs()).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectReturningMethodNoArgs());
    }

    @Test
    public void should_serialize_method_call_with_parameters_that_are_serializable() throws Exception {
        List<?> value = Collections.emptyList();
        when(imethodsMock.objectArgMethod(value)).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectArgMethod(value));
    }

    @Test
    public void should_serialize_method_calls_using_any_string_matcher() throws Exception {
        List<?> value = Collections.emptyList();
        when(imethodsMock.objectArgMethod(anyString())).thenReturn(value);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(value, readObject.objectArgMethod(""));
    }

    @Test
    public void should_verify_called_n_times_for_serialized_mock() throws Exception {
        List<?> value = Collections.emptyList();
        when(imethodsMock.objectArgMethod(anyString())).thenReturn(value);
        imethodsMock.objectArgMethod("");

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        verify(readObject, times(1)).objectArgMethod("");
    }

    @Test
    public void should_verify_even_if_some_methods_called_after_serialization() throws Exception {

        // when
        imethodsMock.simpleMethod(1);
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);
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
    public void should_serialization_work() throws Exception {
        //given
        Foo foo = new Foo();
        //when
        foo = serializeAndBack(foo);
        //then
        assertSame(foo, foo.bar.foo);
    }

    @Test
    public void should_stub_even_if_some_methods_called_after_serialization() throws Exception {
        //given
        // when
        when(imethodsMock.simpleMethod(1)).thenReturn("foo");
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        when(readObject.simpleMethod(2)).thenReturn("bar");

        // then
        assertEquals("foo", readObject.simpleMethod(1));
        assertEquals("bar", readObject.simpleMethod(2));
    }

    @Test
    public void should_verify_call_order_for_serialized_mock() throws Exception {
        imethodsMock.arrayReturningMethod();
        imethodsMock2.arrayReturningMethod();

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);
        ByteArrayOutputStream serialized2 = serializeMock(imethodsMock2);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        IMethods readObject2 = deserializeMock(serialized2, IMethods.class);
        InOrder inOrder = inOrder(readObject, readObject2);
        inOrder.verify(readObject).arrayReturningMethod();
        inOrder.verify(readObject2).arrayReturningMethod();
    }

    @Test
    public void should_remember_interactions_for_serialized_mock() throws Exception {
        List<?> value = Collections.emptyList();
        when(imethodsMock.objectArgMethod(anyString())).thenReturn(value);
        imethodsMock.objectArgMethod("happened");

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        verify(readObject, never()).objectArgMethod("never happened");
    }

    @Test
    public void should_serialize_with_stubbing_callback() throws Exception {

        // given
        CustomAnswersMustImplementSerializableForSerializationToWork answer = 
            new CustomAnswersMustImplementSerializableForSerializationToWork();
        answer.string = "return value";
        when(imethodsMock.objectArgMethod(anyString())).thenAnswer(answer);

        // when
        ByteArrayOutputStream serialized = serializeMock(imethodsMock);

        // then
        IMethods readObject = deserializeMock(serialized, IMethods.class);
        assertEquals(answer.string, readObject.objectArgMethod(""));
    }

    static class CustomAnswersMustImplementSerializableForSerializationToWork
        implements Answer<Object>, Serializable {
        private String string;
        public Object answer(InvocationOnMock invocation) throws Throwable {
            invocation.getArguments();
            invocation.getMock();
            return string;
        }
    }

    @Test
    public void should_serialize_with_real_object_spy() throws Exception {
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
    public void should_serialize_object_mock() throws Exception {
        // when
        ByteArrayOutputStream serialized = serializeMock(any);

        // then
        deserializeMock(serialized, Any.class);
    }
    
    @Test
    public void should_serialize_real_partial_mock() throws Exception {
        // given
        when(anyMock.matches(anyObject())).thenCallRealMethod();

        // when
        ByteArrayOutputStream serialized = serializeMock(anyMock);

        // then
        Any readObject = deserializeMock(serialized, Any.class);
        readObject.matches("");
    }

    class AlreadySerializable implements Serializable {}

    @Test
    public void should_serialize_already_serializable_class() throws Exception {
        // given
        when(alreadySerializableMock.toString()).thenReturn("foo");

        // when
        alreadySerializableMock = serializeAndBack(alreadySerializableMock);

        // then
        assertEquals("foo", alreadySerializableMock.toString());
    }
    
    @Test
    public void should_be_serialize_and_have_extra_interfaces() throws Exception {
        //then
        Assertions.assertThat((Object) serializeAndBack((List) imethodsWithExtraInterfacesMock))
                .isInstanceOf(List.class)
                .isInstanceOf(IMethods.class);
    }



    static class NotSerializableAndNoDefaultConstructor {
        NotSerializableAndNoDefaultConstructor(Observable o) { super(); }
    }



    static class SerializableAndNoDefaultConstructor implements Serializable {
        SerializableAndNoDefaultConstructor(Observable o) { super(); }
    }

    public static class TestClassThatHoldValidField {
        @Mock(serializable=true)
        SerializableAndNoDefaultConstructor serializableAndNoDefaultConstructor;
    }

    @Test
    public void should_be_able_to_serialize_type_that_implements_Serializable_but_but_dont_declare_a_no_arg_constructor() throws Exception {
        TestClassThatHoldValidField testClass = new TestClassThatHoldValidField();
        MockitoAnnotations.initMocks(testClass);

        serializeAndBack(testClass.serializableAndNoDefaultConstructor);
    }
}
