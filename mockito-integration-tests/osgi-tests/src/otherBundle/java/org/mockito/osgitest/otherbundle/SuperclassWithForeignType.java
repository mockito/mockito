/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.otherbundle;

import org.mockito.osgitest.depbundle.ForeignType;

/**
 * Superclass defined by another bundle that exposes a type ({@link ForeignType})
 * which the subclass's bundle has no reason to import.
 */
public class SuperclassWithForeignType {

    public void setForeignType(ForeignType foreignType) {}

    public String name() {
        return "super";
    }
}
