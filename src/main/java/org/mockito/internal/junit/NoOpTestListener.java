/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;

public class NoOpTestListener implements MockitoTestListener {

    public void testFinished(TestFinishedEvent event) {}

    public void onMockCreated(Object mock, MockCreationSettings settings) {}
}
