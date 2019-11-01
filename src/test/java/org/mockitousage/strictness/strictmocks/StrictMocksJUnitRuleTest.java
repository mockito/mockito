/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness.strictmocks;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnstubbedInvocation;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

public class StrictMocksJUnitRuleTest {

    @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_MOCKS);
    @Mock IMethods mock;

    @Test public void unstubbed_invocation_is_detected() {
        assertThatExceptionOfType(UnstubbedInvocation.class).isThrownBy(() -> mock.simpleMethod("foo"));
    }
}
