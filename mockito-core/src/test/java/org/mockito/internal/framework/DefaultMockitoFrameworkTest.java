/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockitoutil.ThrowableAssert.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.Preconditions;
import org.mockito.ArgumentMatchers;
import org.mockito.MockSettings;
import org.mockito.MockedStatic;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.DisabledMockException;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.listeners.MockCreationListener;
import org.mockito.listeners.MockitoListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InlineMockMaker;
import org.mockitoutil.TestBase;

class PersonWithName {
    private final String myName;

    PersonWithName(String name) {
        myName = Preconditions.notNull(name, "non-null name");
    }

    public String getMyName() {
        return myName.toUpperCase();
    }
}

class HasStatic {
    public static String staticName() {
        return "static name";
    }
}

public class DefaultMockitoFrameworkTest extends TestBase {

    private DefaultMockitoFramework framework = new DefaultMockitoFramework();

    @After
    public void clearListeners() {
        new StateMaster().clearMockitoListeners();
    }

    @Test
    public void prevents_adding_null_listener() {
        assertThatThrownBy(
                        () -> {
                            framework.addListener(null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("listener should not be null");
    }

    @Test
    public void prevents_removing_null_listener() {
        assertThatThrownBy(
                        () -> {
                            framework.removeListener(null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("listener should not be null");
    }

    @Test
    public void ok_to_remove_unknown_listener() {
        // it is safe to remove listener that was not added before
        framework.removeListener(new MockitoListener() {});
    }

    @Test
    public void ok_to_remove_listener_multiple_times() {
        MockitoListener listener = new MockitoListener() {};

        // when
        framework.addListener(listener);

        // then it is ok to:
        framework.removeListener(listener);
        framework.removeListener(listener);
    }

    @Test
    public void adds_creation_listener() {
        // given creation listener is added
        MockCreationListener listener = mock(MockCreationListener.class);
        framework.addListener(listener);

        // when
        MockSettings settings = withSettings().name("my list");
        List mock = mock(List.class, settings);
        Set mock2 = mock(Set.class);

        // then
        verify(listener).onMockCreated(eq(mock), any(MockCreationSettings.class));
        verify(listener).onMockCreated(eq(mock2), any(MockCreationSettings.class));
        verifyNoMoreInteractions(listener);
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void removes_creation_listener() {
        // given creation listener is added
        MockCreationListener listener = mock(MockCreationListener.class);
        framework.addListener(listener);

        // and hooked up correctly
        mock(List.class);
        verify(listener).onMockCreated(ArgumentMatchers.any(), any(MockCreationSettings.class));

        // when
        framework.removeListener(listener);
        mock(Set.class);

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void prevents_duplicate_listeners_of_the_same_type() {
        // given creation listener is added
        framework.addListener(new MyListener());

        assertThat(
                        new Runnable() {
                            @Override
                            public void run() {
                                framework.addListener(new MyListener());
                            }
                        })
                .throwsException(RedundantListenerException.class)
                .throwsMessage(
                        "\n"
                                + "Problems adding Mockito listener.\n"
                                + "Listener of type 'MyListener' has already been added and not removed.\n"
                                + "It indicates that previous listener was not removed according to the API.\n"
                                + "When you add a listener, don't forget to remove the listener afterwards:\n"
                                + "  Mockito.framework().removeListener(myListener);\n"
                                + "For more information, see the javadoc for RedundantListenerException class.");
    }

    @Test
    public void clearing_all_mocks_is_safe_regardless_of_mock_maker_type() {
        List mock = mock(List.class);

        // expect
        assertTrue(mockingDetails(mock).isMock());
        framework.clearInlineMocks();
    }

    @Test
    public void behavior_after_clear_inline_mocks() {
        // clearing mocks only works with inline mocking
        assumeTrue(Plugins.getMockMaker() instanceof InlineMockMaker);

        PersonWithName obj = mock(PersonWithName.class);
        when(obj.getMyName()).thenReturn("Bob");
        assertEquals("Bob", obj.getMyName());
        assertTrue(mockingDetails(obj).isMock());

        framework.clearInlineMocks();

        try {
            obj.getMyName();
        } catch (DisabledMockException e) {
            return;
        }
        Assert.fail("Should have thrown DisabledMockException");
    }

    @Test
    public void clear_inline_mocks_clears_static_mocks() {
        // disabling mocks only works with inline mocking
        assumeTrue(Plugins.getMockMaker() instanceof InlineMockMaker);
        assertEquals("static name", HasStatic.staticName());

        // create a static mock
        MockedStatic<HasStatic> mocked = mockStatic(HasStatic.class);

        mocked.when(HasStatic::staticName).thenReturn("hacked name");
        assertEquals("hacked name", HasStatic.staticName());

        framework.clearInlineMocks();

        assertEquals("static name", HasStatic.staticName());
    }

    @Test
    public void behavior_after_clear_inline_mocks_is_mock() {
        // clearing mocks only works with inline mocking
        assumeTrue(Plugins.getMockMaker() instanceof InlineMockMaker);

        PersonWithName obj = mock(PersonWithName.class);
        when(obj.getMyName()).thenReturn("Bob");
        assertEquals("Bob", obj.getMyName());
        assertTrue(mockingDetails(obj).isMock());

        framework.clearInlineMocks();

        try {
            mockingDetails(obj).isMock();
        } catch (DisabledMockException e) {
            return;
        }
        Assert.fail("Should have thrown DisabledMockException");
    }

    @Test
    public void clears_mock() {
        // clearing mocks only works with inline mocking
        assumeTrue(Plugins.getMockMaker() instanceof InlineMockMaker);

        // given
        List list1 = mock(List.class);
        assertTrue(mockingDetails(list1).isMock());
        List list2 = mock(List.class);
        assertTrue(mockingDetails(list2).isMock());

        // when
        framework.clearInlineMock(list1);

        // then
        assertThrows(DisabledMockException.class, () -> mockingDetails(list1).isMock());
        assertTrue(mockingDetails(list2).isMock());
    }

    @Test
    public void clears_mocks() {
        // clearing mocks only works with inline mocking
        assumeTrue(Plugins.getMockMaker() instanceof InlineMockMaker);

        // given
        List list1 = mock(List.class);
        assertTrue(mockingDetails(list1).isMock());
        List list2 = mock(List.class);
        assertTrue(mockingDetails(list2).isMock());

        framework.clearInlineMocks();

        // then
        assertThrows(DisabledMockException.class, () -> mockingDetails(list1).isMock());
        assertThrows(DisabledMockException.class, () -> mockingDetails(list2).isMock());
    }

    private static class MyListener implements MockitoListener {}
}
