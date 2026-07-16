/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.depbundle;

/**
 * A type that only lives in this bundle. It is referenced by a superclass in another bundle but is
 * not imported by the bundle that defines the mocked subclass, reproducing issue #2694.
 */
public class ForeignType {}
