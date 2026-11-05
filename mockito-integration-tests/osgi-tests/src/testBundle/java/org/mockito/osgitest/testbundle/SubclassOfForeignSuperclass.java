/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.mockito.osgitest.otherbundle.SuperclassWithForeignType;

/**
 * Subclass defined by the test bundle. It never touches {@code ForeignType}
 * directly, so its bundle does not import that package.
 */
public class SubclassOfForeignSuperclass extends SuperclassWithForeignType {}
