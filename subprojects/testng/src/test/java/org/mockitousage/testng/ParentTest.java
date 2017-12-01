/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.testng;

import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;

import java.util.Map;

@Listeners(MockitoTestNGListener.class)
public abstract class ParentTest {

    @Mock Map parentMockField;


}
