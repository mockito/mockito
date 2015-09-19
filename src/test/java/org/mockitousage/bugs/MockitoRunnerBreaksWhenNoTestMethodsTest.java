/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;


// @Ignore("for demo only. this test cannot be enabled as it fails :)")
public class MockitoRunnerBreaksWhenNoTestMethodsTest extends TestBase {

    @Test
    public void ensure_the_test_runner_breaks() throws Exception {
        JUnitCore runner = new JUnitCore();
        runner.addListener(new TextListener(System.out));

        Result result = runner.run(TestClassWithoutTestMethod.class);

        assertEquals(1, result.getFailureCount());
        assertFalse(result.wasSuccessful());
    }

    @RunWith(MockitoJUnitRunner.class)
    static class TestClassWithoutTestMethod { // package visibility is important
        public void notATestMethod() { }
    }

}