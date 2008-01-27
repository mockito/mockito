/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.cause;

import org.mockito.exceptions.base.MockitoException;

public class WantedAnywhereAfterFollowingInteraction extends MockitoException {

    public WantedAnywhereAfterFollowingInteraction(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;

}
