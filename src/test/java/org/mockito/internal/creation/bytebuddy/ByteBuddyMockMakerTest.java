package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.verify;
import java.util.Collections;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockitoutil.TestBase;

public class ByteBuddyMockMakerTest extends TestBase {

    @InjectMocks
    private ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

    @Mock
    private MockMaker delegate;

    @Test
    public void should_delegate_call() {
        CreationSettings<Object> creationSettings = new CreationSettings<Object>();
        MockHandlerImpl<Object> handler = new MockHandlerImpl<Object>(creationSettings);

        mockMaker.createMockType(getClass(), Collections.<Class<?>>emptySet(), SerializableMode.ACROSS_CLASSLOADERS);
        mockMaker.createMock(creationSettings, handler);
        mockMaker.getMockedType(this);
        mockMaker.getHandler(this);
        mockMaker.isTypeMockable(Object.class);
        mockMaker.resetMock(this, handler, creationSettings);

        verify(delegate).createMockType(getClass(), Collections.<Class<?>>emptySet(), SerializableMode.ACROSS_CLASSLOADERS);
        verify(delegate).createMock(creationSettings, handler);
        verify(delegate).getMockedType(this);
        verify(delegate).getHandler(this);
        verify(delegate).isTypeMockable(Object.class);
        verify(delegate).resetMock(this, handler, creationSettings);
    }
}
