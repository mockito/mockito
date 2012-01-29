/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.plugins;

/**
 * This API is incubating. It may change.
 * <p>
 * Informs about the mock settings
 */
public interface MockSettingsInfo {

    /**
     * if the mock is serializable
     */
    boolean isSerializable();
}
