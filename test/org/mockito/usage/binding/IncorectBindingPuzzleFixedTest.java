package org.mockito.usage.binding;

import org.junit.*;
import org.mockito.Mockito;

public class IncorectBindingPuzzleFixedTest {

    private DerivedInterface mock;
    
    @Before public void setup() {
        mock = Mockito.mock(DerivedInterface.class);
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
        Message message = new Message();
        print(message);
        Mockito.verify(mock).print(message);
    }
    
    @Test
    public void overriddenInterfaceMethodWorking() throws Exception {
        Message message = new Message();
        print(message);
        Mockito.verify(mock).print((BaseMessage)message);
    }
}