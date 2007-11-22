package org.mockito.usage.binding;

import org.junit.*;
import org.mockito.Mockito;

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

    @Ignore
    @Test
    public void overriddenInterfaceMethodNotWorking() throws Exception {
        DerivedInterface derivedMock = Mockito.mock(DerivedInterface.class);
        setMock(derivedMock);
        Message message = new Message();
        print(message);
        Mockito.verify(derivedMock).print(message);
    }

    @Test
    public void overriddenInterfaceMethodWorking() throws Exception {
        DerivedInterface derivedMock = Mockito.mock(DerivedInterface.class);
        setMock(derivedMock);
        BaseMessage message = new Message();
        print(message);
        Mockito.verify(derivedMock).print(message);
    }

}