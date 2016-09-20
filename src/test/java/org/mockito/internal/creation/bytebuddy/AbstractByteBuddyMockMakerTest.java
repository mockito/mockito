package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.Answer;
import org.mockitoutil.ClassLoaders;
import org.mockitoutil.SimpleSerializationUtil;
import org.objenesis.ObjenesisStd;

import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockitoutil.ClassLoaders.coverageTool;
import static org.mockitoutil.SimpleSerializationUtil.serializeAndBack;

public abstract class AbstractByteBuddyMockMakerTest {

    protected final MockMaker mockMaker;

    public AbstractByteBuddyMockMakerTest(MockMaker mockMaker) {
        this.mockMaker = mockMaker;
    }

    protected abstract Class<?> mockTypeOf(Class<?> type);

    @Test
    public void should_create_mock_from_interface() throws Exception {
        SomeInterface proxy = mockMaker.createMock(settingsFor(SomeInterface.class), dummyH());

        Class<?> superClass = proxy.getClass().getSuperclass();
        assertThat(superClass).isEqualTo(Object.class);
    }


    @Test
    public void should_create_mock_from_class() throws Exception {
        ClassWithoutConstructor proxy = mockMaker.createMock(settingsFor(ClassWithoutConstructor.class), dummyH());

        Class<?> superClass = mockTypeOf(proxy.getClass());
        assertThat(superClass).isEqualTo(ClassWithoutConstructor.class);
    }

    @Test
    public void should_create_mock_from_class_even_when_constructor_is_dodgy() throws Exception {
        try {
            new ClassWithDodgyConstructor();
            fail();
        } catch (Exception expected) {}

        ClassWithDodgyConstructor mock = mockMaker.createMock(settingsFor(ClassWithDodgyConstructor.class), dummyH());
        assertThat(mock).isNotNull();
    }

    @Test
    public void should_mocks_have_different_interceptors() throws Exception {
        SomeClass mockOne = mockMaker.createMock(settingsFor(SomeClass.class), dummyH());
        SomeClass mockTwo = mockMaker.createMock(settingsFor(SomeClass.class), dummyH());

        MockHandler handlerOne = mockMaker.getHandler(mockOne);
        MockHandler handlerTwo = mockMaker.getHandler(mockTwo);


        assertThat(handlerOne).isNotSameAs(handlerTwo);
    }

    @Test
    public void should_use_ancillary_Types() {
        SomeClass mock = mockMaker.createMock(settingsFor(SomeClass.class, SomeInterface.class), dummyH());

        assertThat(mock).isInstanceOf(SomeInterface.class);
    }

    @Test
    public void should_create_class_by_constructor() {
        OtherClass mock = mockMaker.createMock(settingsWithConstructorFor(OtherClass.class), dummyH());
        assertThat(mock).isNotNull();
    }

    @Test
    public void should_allow_serialization() throws Exception {
        SerializableClass proxy = mockMaker.createMock(serializableSettingsFor(SerializableClass.class, SerializableMode.BASIC), dummyH());

        SerializableClass serialized = SimpleSerializationUtil.serializeAndBack(proxy);
        assertThat(serialized).isNotNull();

        MockHandler handlerOne = mockMaker.getHandler(proxy);
        MockHandler handlerTwo = mockMaker.getHandler(serialized);

        assertThat(handlerOne).isNotSameAs(handlerTwo);
    }

    class SomeClass {}
    interface SomeInterface {}
    static class OtherClass {}
    static class SerializableClass implements Serializable {}

    private class ClassWithoutConstructor {}

    private class ClassWithDodgyConstructor {
        public ClassWithDodgyConstructor() {
            throw new RuntimeException();
        }
    }

    @Test
    public void instantiate_fine_when_objenesis_on_the_classpath() throws Exception {
        // given
        ClassLoader classpath_with_objenesis = ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class, ObjenesisStd.class)
                .withCodeSourceUrlOf(coverageTool())
                .build();

        Class<?> mock_maker_class_loaded_fine_until = Class.forName(
                "org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker",
                true,
                classpath_with_objenesis
        );

        // when
        mock_maker_class_loaded_fine_until.newInstance();

        // then everything went fine
    }

    private static <T> MockCreationSettings<T> settingsFor(Class<T> type, Class<?>... extraInterfaces) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        if(extraInterfaces.length > 0) mockSettings.extraInterfaces(extraInterfaces);
        return mockSettings;
    }

    private static <T> MockCreationSettings<T> serializableSettingsFor(Class<T> type, SerializableMode serializableMode) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.serializable(serializableMode);
        mockSettings.setTypeToMock(type);
        return mockSettings;
    }

    private static <T> MockCreationSettings<T> settingsWithConstructorFor(Class<T> type) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        return mockSettings;
    }

    private static MockHandler dummyH() {
        return new DummyMockHandler();
    }

    private static class DummyMockHandler implements InternalMockHandler<Object> {
        public Object handle(Invocation invocation) throws Throwable { return null; }
        public MockCreationSettings<Object> getMockSettings() { return null; }
        public InvocationContainer getInvocationContainer() { return null; }
        public void setAnswersForStubbing(List<Answer<?>> list) { }
    }
}
