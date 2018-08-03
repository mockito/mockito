/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.MockSettings;
import org.mockito.quality.Strictness;

/**
 * Fails early when mismatched arguments used for stubbing
 */
public class StrictStubsRunnerTestListener implements MockitoTestListener {

    private final DefaultStubbingLookupListener stubbingLookupListener = new DefaultStubbingLookupListener(Strictness.STRICT_STUBS);

    @Override
    public void testFinished(TestFinishedEvent event) {}

    @Override
    public void onMockCreated(Object mock, MockSettings settings) {
        settings.addListeners(stubbingLookupListener);
    }
}
