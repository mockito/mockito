/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;

/**
 * Fails early when mismatched arguments used for stubbing
 */
public class StrictStubsRunnerTestListener implements MockitoTestListener {

    private final DefaultStubbingLookupListener stubbingLookupListener =
            new DefaultStubbingLookupListener(Strictness.STRICT_STUBS);

    @Override
    public void testFinished(TestFinishedEvent event) {}

    @Override
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        // It is not ideal that we modify the state of MockCreationSettings object
        // MockCreationSettings is intended to be an immutable view of the creation settings
        // However, we our previous listeners work this way and it hasn't backfired.
        // Since it is simple and pragmatic, we'll keep it for now.
        settings.getStubbingLookupListeners().add(stubbingLookupListener);
    }
}
