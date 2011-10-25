/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.After;
import org.junit.Test;
import org.mockito.internal.verification.DummyVerificationMode;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThreadSafeMockingProgressTest extends TestBase {

    @After
    public void after() {
        this.resetState();
    }

    @Test
    public void shouldShareState() throws Exception {
        //given
        ThreadSafeMockingProgress p = new ThreadSafeMockingProgress();
        p.verificationStarted(new DummyVerificationMode());

        //then
        p = new ThreadSafeMockingProgress();
        assertNotNull(p.pullVerificationMode());
    }

    @Test
    public void shouldKnowWhenVerificationHasStarted() throws Exception {
        //given
        verify(mock(List.class));
        ThreadSafeMockingProgress p = new ThreadSafeMockingProgress();

        //then
        assertNotNull(p.pullVerificationMode());
    }
}
