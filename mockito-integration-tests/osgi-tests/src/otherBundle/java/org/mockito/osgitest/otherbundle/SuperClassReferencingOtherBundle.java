/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.otherbundle;

import org.mockito.osgitest.depbundle.ForeignType;

/**
 * A superclass that exposes {@link ForeignType} from a third bundle. This bundle imports the
 * package of {@code ForeignType}, but the bundle that defines the mocked subclass does not.
 */
public class SuperClassReferencingOtherBundle {

    public ForeignType foreignType() {
        return null;
    }
}
