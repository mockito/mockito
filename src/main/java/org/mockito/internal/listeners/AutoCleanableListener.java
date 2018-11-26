/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

/**
 * Listener that is automatically cleaned up (removed from the the global list of subscribers).
 * For now, we don't intend to make this interface public.
 */
public interface AutoCleanableListener {

    /**
     * Indicates that the listener is dirty and should be removed from the subscribers
     */
    boolean isListenerDirty();
}
