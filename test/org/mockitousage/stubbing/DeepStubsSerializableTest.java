package org.mockitousage.stubbing;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockitoutil.SimpleSerializationUtil;

public class DeepStubsSerializableTest {

    public static final boolean STUBBED_BOOLEAN_VALUE = true;
    public static final int STUBBED_INTEGER_VALUE = 999;

    @Test
    public void should_serialize_and_deserialize_mock_created_by_deep_stubs() throws Exception {
        // given
        SampleClass sampleClass = mock(SampleClass.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).serializable());
        when(sampleClass.getSample().isSth()).thenReturn(STUBBED_BOOLEAN_VALUE);
        when(sampleClass.getSample().getNumber()).thenReturn(STUBBED_INTEGER_VALUE);

        // when
        Object o = SimpleSerializationUtil.serializeAndBack(sampleClass);

        // then
        assertThat(o).isInstanceOf(SampleClass.class);
        SampleClass deserializedSample = (SampleClass) o;
        assertThat(deserializedSample.getSample().isSth()).isEqualTo(STUBBED_BOOLEAN_VALUE);
        assertThat(deserializedSample.getSample().getNumber()).isEqualTo(STUBBED_INTEGER_VALUE);
    }

	@Test
	@Ignore
	public void should_serialize_and_deserialize_map_mocked_by_deep_stubs() throws Exception {
		// given
		Map map = Mockito.mock(Map.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).serializable());
		Mockito.when(map.entrySet().contains(Matchers.anyString())).thenReturn(STUBBED_BOOLEAN_VALUE);

		// when
		Object o = SimpleSerializationUtil.serializeAndBack(map);
		
		// then
		assertThat(o).isInstanceOf(Map.class);
		Map deserializedMap = (Map) o;
		assertThat(deserializedMap.entrySet().contains("Something")).isEqualTo(STUBBED_BOOLEAN_VALUE);
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
	    int getNumber(){
		    return 100; 
	    }
    }

}
