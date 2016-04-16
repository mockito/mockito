/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;


/**
 * Provides information about the invocation, specifically a human readable description and the location.
 */
public interface DescribedInvocation {

    /**
     * Describes the invocation in the human friendly way.
     *
     * @return the description of this invocation.
     */
    String toString();

    /**
     * The place in the code where the invocation happened.
     *
     * @return the location of the invocation.
     */
    Location getLocation();
}
