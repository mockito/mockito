/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.TestRule;

/**
 * Equivalent to {@link MockitoRule}, but also inherits from {@link TestRule}. For more information,
 * please see the documentation on {@link MockitoRule}.
 */
public interface MockitoTestRule extends MockitoRule, TestRule {

}
