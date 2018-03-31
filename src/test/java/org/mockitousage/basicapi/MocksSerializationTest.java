/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.basicapi;

import net.bytebuddy.ClassFileVersion;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.SimpleSerializationUtil;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;
import static org.mockitoutil.SimpleSerializationUtil.*;

@SuppressWarnings({"unchecked", "serial"})
public class MocksSerializationTest extends TestBase implements Serializable {

    private static final long serialVersionUID = 6160482220413048624L;

    @Test
    public void should_allow_throws_exception_to_be_serializable() throws Exception {
        // given
        Bar mock = mock(Bar.class, new ThrowsException(new RuntimeException()));
        // when-serialize then-deserialize
        serializeAndBack(mock);
    }

    @Test
    public void should_allow_method_delegation() throws Exception {
        // given
        Bar barMock = mock(Bar.class, withSettings().serializable());
        Foo fooMock = mock(Foo.class);
        when(barMock.doSomething()).thenAnswer(new ThrowsException(new RuntimeException()));

        //when-serialize then-deserialize
        serializeAndBack(barMock);
    }

    @Test
    public void should_allow_mock_to_be_serializable() throws Exception {
        // given
        IMethods mock = mock(IMethods.class, withSettings().serializable());

        // when-serialize then-deserialize
        serializeAndBack(mock);
    }

    @Test
    public void should_allow_mock_and_boolean_value_to_serializable() throws Exception {
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
    public void should_allow_mock_and_string_value_to_be_serializable() throws Exception {
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
    public void should_all_mock_and_serializable_value_to_be_serialized() throws Exception {
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
    public void should_serialize_method_call_with_parameters_that_are_serializable() throws Exception {
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
    public void should_serialize_method_calls_using_any_string_matcher() throws Exception {
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
    public void should_verify_called_n_times_for_serialized_mock() throws Exception {
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
    public void should_verify_even_if_some_methods_called_after_serialization() throws Exception {
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
    public void should_verify_call_order_for_serialized_mock() throws Exception {
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
    public void should_remember_interactions_for_serialized_mock() throws Exception {
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
    public void should_serialize_with_stubbing_callback() throws Exception {

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
    public void should_serialize_with_real_object_spy() throws Exception {
        // given
        SerializableClass sample = new SerializableClass();
        SerializableClass spy = mock(SerializableClass.class, withSettings()
                .spiedInstance(sample)
                .defaultAnswer(CALLS_REAL_METHODS)
                .serializable());
        when(spy.foo()).thenReturn("foo");

        // when
        ByteArrayOutputStream serialized = serializeMock(spy);

        // then
        SerializableClass readObject = deserializeMock(serialized, SerializableClass.class);
        assertEquals("foo", readObject.foo());
    }

    @Test
    public void should_serialize_object_mock() throws Exception {
        // given
        Any mock = mock(Any.class);

        // when
        ByteArrayOutputStream serialized = serializeMock(mock);

        // then
        deserializeMock(serialized, Any.class);
    }

    @Test
    public void should_serialize_real_partial_mock() throws Exception {
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
    public void should_serialize_already_serializable_class() throws Exception {
        // given
        AlreadySerializable mock = mock(AlreadySerializable.class, withSettings().serializable());
        when(mock.toString()).thenReturn("foo");

        // when
        mock = serializeAndBack(mock);

        // then
        assertEquals("foo", mock.toString());
    }

    @Test
    public void should_be_serialize_and_have_extra_interfaces() throws Exception {
        //when
        IMethods mock = mock(IMethods.class, withSettings().serializable().extraInterfaces(List.class));
        IMethods mockTwo = mock(IMethods.class, withSettings().extraInterfaces(List.class).serializable());

        //then
        Assertions.assertThat((Object) serializeAndBack((List) mock))
                .isInstanceOf(List.class)
                .isInstanceOf(IMethods.class);
        Assertions.assertThat((Object) serializeAndBack((List) mockTwo))
                .isInstanceOf(List.class)
                .isInstanceOf(IMethods.class);
    }

    static class SerializableAndNoDefaultConstructor implements Serializable {
        SerializableAndNoDefaultConstructor(Observable o) { super(); }
    }

    @Test
    public void should_be_able_to_serialize_type_that_implements_Serializable_but_but_dont_declare_a_no_arg_constructor() throws Exception {
        serializeAndBack(mock(SerializableAndNoDefaultConstructor.class));
    }



    public static class AClassWithPrivateNoArgConstructor {
        private AClassWithPrivateNoArgConstructor() {}
        List returningSomething() { return Collections.emptyList(); }
    }

    @Test
    public void private_constructor_currently_not_supported_at_the_moment_at_deserialization_time() throws Exception {
        // given
        AClassWithPrivateNoArgConstructor mockWithPrivateConstructor = Mockito.mock(
                AClassWithPrivateNoArgConstructor.class,
                Mockito.withSettings().serializable()
        );

        try {
            // when
            SimpleSerializationUtil.serializeAndBack(mockWithPrivateConstructor);
            fail("should have thrown an ObjectStreamException or a subclass of it");
        } catch (ObjectStreamException e) {
            // then
            Assertions.assertThat(e.toString()).contains("no valid constructor");
        }
    }


    @Test
    public void BUG_ISSUE_399_try_some_mocks_with_current_answers() throws Exception {
        assumeTrue(ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V7));

        IMethods iMethods = mock(IMethods.class, withSettings().serializable().defaultAnswer(RETURNS_DEEP_STUBS));

        when(iMethods.iMethodsReturningMethod().linkedListReturningMethod().contains(anyString())).thenReturn(false);

        serializeAndBack(iMethods);
    }

    public static class SerializableClass implements Serializable {

        public String foo() {
            return null;
        }
    }
}
