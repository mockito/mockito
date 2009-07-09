/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

//TODO before final 1.8
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class MockitoRunnerBreaksWhenNoTestMethodsTest extends TestBase {}