package org.mockito.internal.creation.bytebuddy;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.mock.MockCreationSettings;

import static org.assertj.core.api.Assertions.assertThat;

public class InlineByteBuddyMockMakerTest extends AbstractByteBuddyMockMakerTest {

    public InlineByteBuddyMockMakerTest() {
        super(new InlineByteBuddyMockMaker());
    }

    @Override
    protected Class<?> mockTypeOf(Class<?> type) {
        return type;
    }

    @Test
    public void should_create_mock_from_final_class() throws Exception {
        MockCreationSettings<FinalClass> settings = settingsFor(FinalClass.class);
        FinalClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalClass>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_abstract_class_with_final_method() throws Exception {
        MockCreationSettings<FinalMethodAbstractType> settings = settingsFor(FinalMethodAbstractType.class);
        FinalMethodAbstractType proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalMethodAbstractType>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
        assertThat(proxy.bar()).isEqualTo("bar");
    }

    @Test
    @Ignore("Endless loop is creating when looking and invoking the super method in MockMethodAdvice.SuperMethodCall#invoke")
    public void should_create_mock_from_class_with_super_call_to_final_method() throws Exception {
        MockSettingsImpl<CallingSuperMethodClass> settings = settingsFor(CallingSuperMethodClass.class);
        settings.defaultAnswer(new CallsRealMethods());
        CallingSuperMethodClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<CallingSuperMethodClass>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_final_class_with_interface_methods() throws Exception {
        MockCreationSettings<FinalMethod> settings = settingsFor(FinalMethod.class, SampleInterface.class);
        FinalMethod proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalMethod>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
        assertThat(((SampleInterface) proxy).bar()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_enum() throws Exception {
        MockCreationSettings<EnumClass> settings = settingsFor(EnumClass.class);
        EnumClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<EnumClass>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
    }

    @Test
    public void should_remove_recursive_self_call_from_stack_trace() throws Exception {
        StackTraceElement[] stack = new StackTraceElement[]{
                new StackTraceElement("foo", "", "", -1),
                new StackTraceElement(SampleInterface.class.getName(), "", "", -1),
                new StackTraceElement("qux", "", "", -1),
                new StackTraceElement("bar", "", "", -1),
                new StackTraceElement("baz", "", "", -1)
        };

        Throwable throwable = new Throwable();
        throwable.setStackTrace(stack);
        throwable = MockMethodAdvice.hideRecursiveCall(throwable, 2, SampleInterface.class);

        assertThat(throwable.getStackTrace()).isEqualTo(new StackTraceElement[]{
                new StackTraceElement("foo", "", "", -1),
                new StackTraceElement("bar", "", "", -1),
                new StackTraceElement("baz", "", "", -1)
        });
    }

    @Test
    public void should_handle_missing_or_inconsistent_stack_trace() throws Exception {
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[0]);
        assertThat(MockMethodAdvice.hideRecursiveCall(throwable, 0, SampleInterface.class)).isSameAs(throwable);
    }

    private static <T> MockSettingsImpl<T> settingsFor(Class<T> type, Class<?>... extraInterfaces) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        mockSettings.defaultAnswer(new Returns("bar"));
        if (extraInterfaces.length > 0) mockSettings.extraInterfaces(extraInterfaces);
        return mockSettings;
    }

    private static final class FinalClass {

        public String foo() {
            return "foo";
        }
    }

    private enum EnumClass {

        INSTANCE;

        public String foo() {
            return "foo";
        }
    }

    private abstract static class FinalMethodAbstractType {

        public final String foo() {
            return "foo";
        }

        public abstract String bar();
    }

    private static class FinalMethod {

        public final String foo() {
            return "foo";
        }
    }

    private static class NonFinalMethod {
        public String foo() {
            return "foo";
        }
    }

    private static class CallingSuperMethodClass extends NonFinalMethod {
        @Override
        public String foo() {
            return super.foo();
        }
    }

    private interface SampleInterface {

        String bar();
    }
}
