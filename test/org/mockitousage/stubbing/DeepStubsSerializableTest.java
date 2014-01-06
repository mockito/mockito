package org.mockitousage.stubbing;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;
import org.mockito.Mockito;

public class DeepStubsSerializableTest {

	public static final boolean STUBBED_VALUE = true;

	@Test
	public void should_serialize_and_deserialize_mock_created_by_deep_stubs() throws Exception {
		// given
		SampleClass sampleClass = Mockito.mock(SampleClass.class, withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS).serializable());
		Mockito.when(sampleClass.getSample().isSth()).thenReturn(STUBBED_VALUE);

		// when
		ByteArrayOutputStream serializationStream = serialize(sampleClass);

		// then
		Object o = deserialize(serializationStream);
		assertThat(o).isInstanceOf(SampleClass.class);
		SampleClass deserializedSample = (SampleClass) o;
		assertThat(deserializedSample.getSample().isSth()).isEqualTo(STUBBED_VALUE);
	}

	private ByteArrayOutputStream serialize(SampleClass sampleClass) throws IOException {
		ByteArrayOutputStream serializationStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(serializationStream);
		objectOutputStream.writeObject(sampleClass);
		objectOutputStream.close();
		return serializationStream;
	}

	private Object deserialize(ByteArrayOutputStream serializationStream) throws IOException, ClassNotFoundException {
		ByteArrayInputStream deserializationStream = new ByteArrayInputStream(serializationStream.toByteArray());
		ObjectInputStream is = new ObjectInputStream(deserializationStream);
		return is.readObject();
	}

	class SampleClass implements Serializable {
		SampleClass2 getSample(){
			return new SampleClass2();
		}
	}

	class SampleClass2 implements Serializable {
		boolean isSth(){
			return false;
		}
	}
	
}
