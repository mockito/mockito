package org.mockito.internal.junit;

interface MockitoTestListener {
    void testFinished(TestFinishedEvent event);
}