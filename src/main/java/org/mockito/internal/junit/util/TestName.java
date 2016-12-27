package org.mockito.internal.junit.util;

import org.mockito.internal.junit.TestFinishedEvent;

/**
 * Provides test name
 */
public class TestName {

    public static String getTestName(TestFinishedEvent event) {
        return event.getTestClassInstance().getClass().getSimpleName() + "." + event.getTestMethodName();
    }
}
