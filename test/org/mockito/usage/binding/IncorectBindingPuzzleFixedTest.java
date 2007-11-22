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

    private class BaseMessage { 
        public String toString() {
            return "BaseMessage";
        }
    }
    
    private class Message extends BaseMessage {
        public String toString() {
            return "Message";
        }
    }

    private interface BaseInteface {
        public void print(BaseMessage message);
    }

    private interface DerivedInterface extends BaseInteface {
        public void print(Message message);
    }

    private void print(BaseMessage message) {
        mock.print(message);
    }

    @Ignore
    @Test
    public void overriddenInterfaceMethodNotWorking() throws Exception {
        DerivedInterface derivedMock = Mockito.mock(DerivedInterface.class);
        setMock(derivedMock);
        Message message = new Message();
        print(message);
        try {
            Mockito.verify(derivedMock).print(message);
        } catch (VerificationAssertionError error) {
            String expected = "\n" +
            		"Not invoked: DerivedInterface.print(Message)" +
            		"But found: DerivedInterface.print(BaseMessage)";
            assertEquals(expected, error.getMessage());
        }
    }
    
    @Ignore
    @Test
    public void shouldReportNoMoreInteractionsProperly() throws Exception {
        DerivedInterface derivedMock = Mockito.mock(DerivedInterface.class);
        setMock(derivedMock);
        Message message = new Message();
        print(message);
        try {
            Mockito.verifyNoMoreInteractions(derivedMock);
        } catch (VerificationAssertionError error) {
            String expected = "\n" +
            		"No more interactions expected on DerivedInterface but found: DerivedInterface.print(BaseMessage)";
            assertEquals(expected, error.getMessage());
        }
    }
}