/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.serialization;

import org.junit.Test;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockitoutil.SimpleSerializationUtil.serializeAndBack;

public class DeepStubsSerializableTest {

    @Test
    public void should_serialize_and_deserialize_mock_created_with_deep_stubs() throws Exception {
        // given
        SampleClass sampleClass = mock(SampleClass.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS).serializable());
        when(sampleClass.getSample().isFalse()).thenReturn(true);
        when(sampleClass.getSample().number()).thenReturn(999);

        // when
        SampleClass deserializedSample = serializeAndBack(sampleClass);

        // then
        assertThat(deserializedSample.getSample().isFalse()).isEqualTo(true);
        assertThat(deserializedSample.getSample().number()).isEqualTo(999);
    }

    @Test
    public void should_serialize_and_deserialize_parameterized_class_mocked_with_deep_stubs() throws Exception {
        // given
        ListContainer deep_stubbed = mock(ListContainer.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS).serializable());
        when(deep_stubbed.iterator().next().add("yes")).thenReturn(true);

        // when
        ListContainer deserialized_deep_stub = serializeAndBack(deep_stubbed);

        // then
        assertThat(deserialized_deep_stub.iterator().next().add("not stubbed but mock already previously resolved")).isEqualTo(false);
        assertThat(deserialized_deep_stub.iterator().next().add("yes")).isEqualTo(true);
    }

    @Test(expected = NullPointerException.class)
    public void should_discard_generics_metadata_when_serialized_then_disabling_deep_stubs_with_generics() throws Exception {
        // given
        ListContainer deep_stubbed = mock(ListContainer.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS).serializable());
        when(deep_stubbed.iterator().hasNext()).thenReturn(true);

        ListContainer deserialized_deep_stub = serializeAndBack(deep_stubbed);

        // when stubbing on a deserialized mock
        when(deserialized_deep_stub.iterator().next().get(42)).thenReturn("no");

        // then revert to the default RETURNS_DEEP_STUBS and the code will raise a ClassCastException
    }

    static class SampleClass implements Serializable {

        SampleClass2 getSample() {
            return new SampleClass2();
        }
    }

    static class SampleClass2 implements Serializable {

        boolean isFalse() {
            return false;
        }

        int number() {
            return 100;
        }
    }

    static class Container<E> implements Iterable<E>, Serializable {

        private E e;

        public Container(E e) {
            this.e = e;
        }

        public E get() {
            return e;
        }

        public Iterator<E> iterator() {
            return new Iterator<E>() {
                public boolean hasNext() {
                    return true;
                }

                public E next() {
                    return e;
                }

                public void remove() {
                }
            };
        }
    }

    static class ListContainer extends Container<List<String>> {

        public ListContainer(List<String> list) {
            super(list);
        }
    }
}
