/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.serialization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockitoutil.SimpleSerializationUtil.serializeAndBack;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StrictStubsSerializableTest {

    @Mock(serializable = true) private SampleClass sampleClass;

    @Test
    public void should_serialize_and_deserialize_mock_created_with_serializable_and_strict_stubs() throws Exception {
        // given
        when(sampleClass.isFalse()).thenReturn(true);

        // when
        SampleClass deserializedSample = serializeAndBack(sampleClass);
        // to satisfy strict stubbing
        deserializedSample.isFalse();
        verify(deserializedSample).isFalse();
        verify(sampleClass, never()).isFalse();

        // then
        assertThat(deserializedSample.isFalse()).isEqualTo(true);
        assertThat(sampleClass.isFalse()).isEqualTo(true);
    }

    static class SampleClass implements Serializable {

        boolean isFalse() {
            return false;
        }
    }
}
