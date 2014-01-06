package org.mockitousage.stubbing;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.SimpleSerializationUtil;

public class DeepStubsSerializableTest {

    public static final boolean STUBBED_VALUE = true;

    @Test
    public void should_serialize_and_deserialize_mock_created_by_deep_stubs() throws Exception {
        // given
        SampleClass sampleClass = mock(SampleClass.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).serializable());
        when(sampleClass.getSample().isSth()).thenReturn(STUBBED_VALUE);

        // when
        Object o = SimpleSerializationUtil.serializeAndBack(sampleClass);

        // then
        assertThat(o).isInstanceOf(SampleClass.class);
        SampleClass deserializedSample = (SampleClass) o;
        assertThat(deserializedSample.getSample().isSth()).isEqualTo(STUBBED_VALUE);
    }


    class SampleClass implements Serializable {
        SampleClass2 getSample() {
            return new SampleClass2();
        }
    }

    class SampleClass2 implements Serializable {
        boolean isSth() {
            return false;
        }
    }

}
