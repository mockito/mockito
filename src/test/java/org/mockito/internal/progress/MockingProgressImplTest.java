/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.internal.listeners.AutoCleanableListener;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.listeners.MockitoListener;
import org.mockito.verification.VerificationMode;
import org.mockitoutil.TestBase;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockingProgressImplTest extends TestBase {

    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockingProgress = new MockingProgressImpl();
    }

    @Test
    public void shouldStartVerificationAndPullVerificationMode() throws Exception {
        assertNull(mockingProgress.pullVerificationMode());

        VerificationMode mode = VerificationModeFactory.times(19);

        mockingProgress.verificationStarted(mode);

        assertSame(mode, mockingProgress.pullVerificationMode());

        assertNull(mockingProgress.pullVerificationMode());
    }

    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
        try {
            mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void shouldNotifyListenerSafely() throws Exception {
        //when
        mockingProgress.addListener(null);

        //then no exception is thrown:
        mockingProgress.mockingStarted(null, null);
    }

    @Test
    public void should_not_allow_redundant_listeners() {
        MockitoListener listener1 = mock(MockitoListener.class);
        final MockitoListener listener2 = mock(MockitoListener.class);

        final Set<MockitoListener> listeners = new LinkedHashSet<MockitoListener>();

        //when
        MockingProgressImpl.addListener(listener1, listeners);

        //then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                MockingProgressImpl.addListener(listener2, listeners);
            }
        }).isInstanceOf(RedundantListenerException.class);
    }

    @Test
    public void should_clean_up_listeners_automatically() {
        MockitoListener someListener = mock(MockitoListener.class);
        MyListener cleanListener = mock(MyListener.class);
        MyListener dirtyListener = when(mock(MyListener.class).isListenerDirty()).thenReturn(true).getMock();

        Set<MockitoListener> listeners = new LinkedHashSet<MockitoListener>();

        //when
        MockingProgressImpl.addListener(someListener, listeners);
        MockingProgressImpl.addListener(dirtyListener, listeners);

        //then
        Assertions.assertThat(listeners).containsExactlyInAnyOrder(someListener, dirtyListener);

        //when
        MockingProgressImpl.addListener(cleanListener, listeners);

        //then dirty listener was removed automatically
        Assertions.assertThat(listeners).containsExactlyInAnyOrder(someListener, cleanListener);
    }

    interface MyListener extends MockitoListener, AutoCleanableListener {}
}
