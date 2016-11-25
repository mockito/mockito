package org.mockito.internal.junit;

import org.mockito.listeners.MockCreationListener;

interface MockitoTestListener extends MockCreationListener {
    void testFinished(TestFinishedEvent event);
}