package org.mockito.usage.binding;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationAssertionError;

public class IncorectBindingPuzzleFixedTest {

    private BaseInteface mock;

    private void setMock(DerivedInterface mock) {
        this.mock = mock;
    }

    private class BaseMessage {}
    
    private class Message extends BaseMessage {}

    private interface BaseInteface {
        public void print(BaseMessage message);
    }

    private interface DerivedInterface extends BaseInteface {
        public void print(Message message);
    }

    private void print(BaseMessage message) {
        mock.print(message);
    }

    @Test
    public void overriddenInterfaceMethodNotWorking() throws Exception {
        DerivedInterface derivedMock = Mockito.mock(DerivedInterface.class);
        setMock(derivedMock);
        Message message = new Message();
        print(message);
        try {
            Mockito.verify(derivedMock).print(message);
            fail();
        } catch (VerificationAssertionError error) {
            String expected = 
                "\n" +
        		"Invocation differs from actual:" +
        		"\n" +
        		"Expected: DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$Message)" +
        		"\n" +
        		"Actual:   DerivedInterface.print(class org.mockito.usage.binding.IncorectBindingPuzzleFixedTest$BaseMessage)";
            
            assertEquals(expected, error.getMessage());
        }
    }
}