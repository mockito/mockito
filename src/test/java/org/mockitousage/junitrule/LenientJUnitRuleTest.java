/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.quality.Strictness;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.when;

public class LenientJUnitRuleTest {

    private MockitoLogger explosiveLogger = new MockitoLogger() {
        public void log(Object what) {
            throw new RuntimeException("Silent rule should not write anything to the logger");
        }
    };
    @Mock private IMethods mock;

    @Rule public MockitoRule mockitoRule = new JUnitRule(explosiveLogger, Strictness.LENIENT);

    @Test public void no_warning_for_unused_stubbing() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("1");
    }

    @Test public void no_warning_for_stubbing_arg_mismatch() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(2);
    }

    @Test(expected = IllegalStateException.class) public void no_warning_for_stubbing_arg_mismatch_on_failure() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(2);
        throw new IllegalStateException("hey!");
    }
}
