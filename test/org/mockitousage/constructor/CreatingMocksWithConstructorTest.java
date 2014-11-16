package org.mockitousage.constructor;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.*;

public class CreatingMocksWithConstructorTest extends TestBase {

    static abstract class AbstractMessage {
        private final String message;
        AbstractMessage() {
            this.message = "hey!";
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
    public void can_mock_inner_classes() {
        InnerClass mock = mock(InnerClass.class, withSettings().useConstructor().outerInstance(this).defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("hey!", mock.getMessage());
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
            assertEquals("Unable to create mock instance of type 'HasConstructor'", e.getMessage());
            assertContains("Please ensure it has parameter-less constructor", e.getCause().getMessage());
        }
    }

    @Test
    @Ignore //TODO SF
    public void mocking_inner_classes_with_wrong_outer_instance() {
        fail();
    }

    @Test
    @Ignore //TODO SF
    public void prevents_mocking_interfaces_with_constructor() {
        try {
            //when
            mock(IMethods.class, withSettings().useConstructor());
            //then
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    @Ignore //TODO SF
    public void prevents_across_jvm_serialization_with_constructor() {
        fail();
    }
}
