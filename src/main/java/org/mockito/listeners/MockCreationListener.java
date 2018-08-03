/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.MockSettings;

/**
 * Notified when mock object is created.
 * For more information on listeners see {@link org.mockito.MockitoFramework#addListener(MockitoListener)}.
 */
public interface MockCreationListener extends MockitoListener {

    /**
     * Mock object was just created.
     *
     * @param mock created mock object
     * @param settings the settings of the mock object
     */
    void onMockCreated(Object mock, MockSettings settings);
}
