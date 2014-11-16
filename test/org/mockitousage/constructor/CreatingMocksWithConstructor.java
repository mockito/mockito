package org.mockitousage.constructor;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class CreatingMocksWithConstructor extends TestBase {

    static abstract class AbstractMessage {
        String message = "hey!";
        String getMessage() {
            return message;
        }
    }

    static class Message extends AbstractMessage {}

    @Test
    public void can_create_mock_with_constructor() {
        Message mock = mock(Message.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        //the message is a part of state of the mocked type that gets initialized in constructor
        assertEquals("hey!", mock.getMessage());
    }

    @Test
    public void can_mock_abstract_classes() {
        AbstractMessage mock = mock(AbstractMessage.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        //the message is a part of state of the mocked type that gets initialized in constructor
        assertEquals("hey!", mock.getMessage());
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
