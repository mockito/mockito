package org.mockitousage.constructor;

import java.util.List;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.mock.SerializableMode;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class CreatingMocksWithConstructorTest extends TestBase {

    static abstract class AbstractMessage {
        private final String message;
        AbstractMessage() {
            this.message = "hey!";
        }
        AbstractMessage(String message) {
            this.message = message;
        }
        AbstractMessage(int i) {
            this.message = String.valueOf(i);
        }
        String getMessage() {
            return message;
        }
    }

    static class Message extends AbstractMessage {}
    class InnerClass extends AbstractMessage {}

    @Test
    public void can_create_mock_with_constructor() {
        Message mock = mock(Message.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        //the message is a part of state of the mocked type that gets initialized in constructor
        assertEquals("hey!", mock.getMessage());
    }

    @Test
    public void can_mock_abstract_classes() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("hey!", mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes() {
        AbstractMessage mock = spy(AbstractMessage.class);
        assertEquals("hey!", mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes_with_constructor_args() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor("hello!").defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("hello!", mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes_with_constructor_primitive_args() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor(7).defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("7", mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes_with_constructor_array_of_nulls() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor(new Object[]{null}).defaultAnswer(CALLS_REAL_METHODS));
        assertNull(mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes_with_casted_null() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor((String) null).defaultAnswer(CALLS_REAL_METHODS));
        assertNull(mock.getMessage());
    }

    @Test
    public void can_spy_abstract_classes_with_null_varargs() {
        try {
            mock(AbstractMessage.class, withSettings().useConstructor(null).defaultAnswer(CALLS_REAL_METHODS));
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessageContaining("constructorArgs should not be null. " +
                "If you need to pass null, please cast it to the right type, e.g.: useConstructor((String) null)");
        }
    }

    @Test
    public void can_mock_inner_classes() {
        InnerClass mock = mock(InnerClass.class, withSettings().useConstructor().outerInstance(this).defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("hey!", mock.getMessage());
    }

    public static class ThrowingConstructorClass{
        public ThrowingConstructorClass() {
            throw new RuntimeException();
        }
    }

    @Test
    public void explains_constructor_exceptions() {
        try {
            mock(ThrowingConstructorClass.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasRootCauseInstanceOf(RuntimeException.class);
            assertThat(e.getCause()).hasMessageContaining("Please ensure the target class has a 0-arg constructor and executes cleanly.");
        }
    }

    static class HasConstructor {
        HasConstructor(String x) {}
    }

    @Test
    public void exception_message_when_constructor_not_found() {
        try {
            //when
            spy(HasConstructor.class);
            //then
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessage("Unable to create mock instance of type 'HasConstructor'");
            assertThat(e.getCause()).hasMessageContaining("Please ensure that the target class has a 0-arg constructor.");
        }
    }

    static class Base {}
    static class ExtendsBase extends Base {}
    static class ExtendsExtendsBase extends ExtendsBase {}

    static class UsesBase {
        public UsesBase(Base b) {}
        public UsesBase(ExtendsBase b) {}
    }

    @Test
    public void can_mock_unambigous_constructor_with_inheritence() {
        mock(UsesBase.class, withSettings().useConstructor(new Base()).defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    public void exception_message_when_ambiguous_constructor_found_exact_exists() throws Exception {
        try {
            //when
            mock(UsesBase.class, withSettings().useConstructor(new ExtendsBase()).defaultAnswer(CALLS_REAL_METHODS));
            //then
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessage("Unable to create mock instance of type 'UsesBase'");
            assertThat(e.getCause()).hasMessageContaining
                ("Multiple constructors could be matched to arguments of types [org.mockitousage.constructor.CreatingMocksWithConstructorTest$ExtendsBase]");
        }
    }

    @Test
    public void fail_when_multiple_matching_constructors() {
        try {
            //when
            mock(UsesBase.class, withSettings().useConstructor(new ExtendsExtendsBase()).defaultAnswer(CALLS_REAL_METHODS));
            //then
            fail();
        } catch (MockitoException e) {
            //TODO the exception message includes Mockito internals like the name of the generated class name.
            //I suspect that we could make this exception message nicer.
            assertThat(e).hasMessage("Unable to create mock instance of type 'UsesBase'");
            assertThat(e.getCause())
                .hasMessageContaining("Multiple constructors could be matched to arguments of types [org.mockitousage.constructor.CreatingMocksWithConstructorTest$ExtendsExtendsBase]")
                .hasMessageContaining("If you believe that Mockito could do a better join deciding on which constructor to use, please let us know.\n" +
                    "Ticket 685 contains the discussion and a workaround for ambiguous constructors using inner class.\n" +
                    "See https://github.com/mockito/mockito/issues/685");
        }
    }

    @Test
    public void mocking_inner_classes_with_wrong_outer_instance() {
        try {
            //when
            mock(InnerClass.class, withSettings().useConstructor().outerInstance(123).defaultAnswer(CALLS_REAL_METHODS));
            //then
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessage("Unable to create mock instance of type 'InnerClass'");
            //TODO it would be nice if all useful information was in the top level exception, instead of in the exception's cause
            //also applies to other scenarios in this test
            assertThat(e.getCause()).hasMessageContaining(
                "Please ensure that the target class has a 0-arg constructor"
                    + " and provided outer instance is correct.");
        }
    }

    @Test
    public void mocking_interfaces_with_constructor() {
        //at the moment this is allowed however we can be more strict if needed
        //there is not much sense in creating a spy of an interface
        mock(IMethods.class, withSettings().useConstructor());
        spy(IMethods.class);
    }

    @Test
    public void prevents_across_jvm_serialization_with_constructor() {
        try {
            //when
            mock(AbstractMessage.class, withSettings().useConstructor().serializable(SerializableMode.ACROSS_CLASSLOADERS));
            //then
            fail();
        } catch (MockitoException e) {
            assertEquals("Mocks instantiated with constructor cannot be combined with " + SerializableMode.ACROSS_CLASSLOADERS + " serialization mode.", e.getMessage());
        }
    }

    static abstract class AbstractThing {
        abstract String name();
        String fullName() {
            return "abstract " + name();
        }
    }

    @Test
    public void abstract_method_returns_default() {
        AbstractThing thing = spy(AbstractThing.class);
        assertEquals("abstract null", thing.fullName());
    }

    @Test
    public void abstract_method_stubbed() {
        AbstractThing thing = spy(AbstractThing.class);
        when(thing.name()).thenReturn("me");
        assertEquals("abstract me", thing.fullName());
    }

    @Test
    public void interface_method_stubbed() {
        List<?> list = spy(List.class);
        when(list.size()).thenReturn(12);
        assertEquals(12, list.size());
    }

    @Test
    public void calls_real_interface_method() {
        List list = mock(List.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        assertNull(list.get(1));
    }

    @Test
    public void handles_bridge_methods_correctly() {
        SomeConcreteClass<Integer> testBug = spy(new SomeConcreteClass<Integer>());
        assertEquals("value", testBug.getValue(0));
    }

    public abstract class SomeAbstractClass<T> {

        protected abstract String getRealValue(T value);

        public String getValue(T value) {
            return getRealValue(value);
        }
    }

    public class SomeConcreteClass<T extends Number> extends SomeAbstractClass<T> {

        @Override
        protected String getRealValue(T value) {
            return "value";
        }
    }
}
