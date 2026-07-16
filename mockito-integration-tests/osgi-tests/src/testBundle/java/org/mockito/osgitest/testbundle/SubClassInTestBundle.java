/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.mockito.osgitest.otherbundle.SuperClassReferencingOtherBundle;

/**
 * A subclass whose superclass lives in another bundle. This bundle does not import the bundle that
 * owns {@code ForeignType}, so mocking this type only works if the generated mock's class loader
 * also reaches the superclass' bundle.
 */
public class SubClassInTestBundle extends SuperClassReferencingOtherBundle {}
