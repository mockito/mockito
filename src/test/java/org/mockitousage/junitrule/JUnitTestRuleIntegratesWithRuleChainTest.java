/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;

public class JUnitTestRuleIntegratesWithRuleChainTest {

    @Rule
    public final RuleChain chain = RuleChain.outerRule(MockitoJUnit.rule(this)).around((base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            assertThat(MockUtil.isMock(mock)).isTrue();
            called.set(true);
            base.evaluate();
        }
    });

    @Mock
    public Object mock;

    private AtomicBoolean called = new AtomicBoolean(false);

    @Test
    public void creates_mocks_in_correct_rulechain_ordering() {
        assertThat(MockUtil.isMock(mock)).isTrue();
        assertThat(called.get()).isTrue();
    }
}
