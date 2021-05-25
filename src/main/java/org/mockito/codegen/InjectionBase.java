/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.codegen;

/**
 * This class is required to resolve a method handle lookup for the {@code org.mockito.codegen} package what requires a preexisting class for the package.
 * By defining this class, the JVM (starting from Java 9) assures that this package is a part of the Mockito module such that we gain full access rights.
 */
public class InjectionBase {

    private InjectionBase() {
        throw new UnsupportedOperationException();
    }
}
