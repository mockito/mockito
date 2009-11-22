package org.mockito.internal;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockitoutil.TestBase;

public class MockitoCoreTest extends TestBase {

    MockingProgress mockingProgress = new ThreadSafeMockingProgress();
    MockitoCore core = new MockitoCore();

    @Test
    public void shouldResetOngoingStubbingWhenAsked() throws Exception {
        //when
        core.mock(Object.class, new MockSettingsImpl(), true);

        //then
        assertNull(mockingProgress.pullOngoingStubbing());
    }

    @Test
    public void shouldNOTResetOngoingStubbingWhenAsked() throws Exception {
        //when
        core.mock(Object.class, new MockSettingsImpl(), false);

        //then
        assertNull(mockingProgress.pullOngoingStubbing());
    }
}
