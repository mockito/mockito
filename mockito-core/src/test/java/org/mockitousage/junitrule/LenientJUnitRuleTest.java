/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoRule;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

public class LenientJUnitRuleTest {

    private MockitoLogger explosiveLogger =
            what -> {
                throw new RuntimeException("Silent rule should not write anything to the logger");
            };
    @Mock private IMethods mock;

    @Rule public MockitoRule mockitoRule = new JUnitRule(explosiveLogger, Strictness.LENIENT);

    @Test
    public void no_warning_for_unused_stubbing() {
        when(mock.simpleMethod(1)).thenReturn("1");
    }

    @Test
    public void no_warning_for_stubbing_arg_mismatch() {
        when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(2);
    }

    @Test
    public void no_warning_for_stubbing_arg_mismatch_on_failure() {
        when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(2);
    }
}
