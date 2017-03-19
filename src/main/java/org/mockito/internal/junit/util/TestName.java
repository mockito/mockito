/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
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
