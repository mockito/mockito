package org.mockito.internal.framework;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockSettings;
import org.mockito.StateMaster;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.mock.MockCreationSettings;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultMockitoFrameworkTest extends TestBase {

    private DefaultMockitoFramework framework = new DefaultMockitoFramework();

    @After public void clearListeners() {
        new StateMaster().clearMockitoListeners();
    }

    @Test(expected = IllegalArgumentException.class)
    public void prevents_adding_null_listener() {
        framework.addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void prevents_removing_null_listener() {
        framework.removeListener(null);
    }

    @Test
    public void ok_to_remove_unknown_listener() {
        //it is safe to remove listener that was not added before
        framework.removeListener(new MockitoListener() {});
    }

    @Test
    public void ok_to_remove_listener_multiple_times() {
        MockitoListener listener = new MockitoListener() {};

        //when
        framework.addListener(listener);

        //then it is ok to:
        framework.removeListener(listener);
        framework.removeListener(listener);
    }

    @Test
    public void adds_creation_listener() {
        //given creation listener is added
        MockCreationListener listener = mock(MockCreationListener.class);
        framework.addListener(listener);

        //when
        MockSettings settings = withSettings().name("my list");
        List mock = mock(List.class, settings);
        Set mock2 = mock(Set.class);

        //then
        verify(listener).onMockCreated(eq(mock), any(MockCreationSettings.class));
        verify(listener).onMockCreated(eq(mock2), any(MockCreationSettings.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void removes_creation_listener() {
        //given creation listener is added
        MockCreationListener listener = mock(MockCreationListener.class);
        framework.addListener(listener);

        //and hooked up correctly
        mock(List.class);
        verify(listener).onMockCreated(ArgumentMatchers.any(), any(MockCreationSettings.class));

        //when
        framework.removeListener(listener);
        mock(Set.class);

        //then
        verifyNoMoreInteractions(listener);
    }
}