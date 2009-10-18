/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

@RunWith(MockitoJUnitRunner.class)
@Ignore("for demo only. this test cannot be enabled as it fails :)")
public class MockitoRunnerBreaksWhenNoTestMethodsTest extends TestBase {
    public void notATestMethod() {}
}