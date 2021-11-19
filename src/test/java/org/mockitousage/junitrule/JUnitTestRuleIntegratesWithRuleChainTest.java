/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;

public class JUnitTestRuleIntegratesWithRuleChainTest {

    JUnitCore runner = new JUnitCore();

    @Test
    public void rule_can_be_changed_to_strict() {
        // when
        Result result = runner.run(StrictByDefault.class);

        // then
        JUnitResultAssert.assertThat(result).succeeds(1).fails(1, RuntimeException.class);
    }

    @Test
    public void rule_can_be_changed_to_lenient() {
        // when
        Result result = runner.run(LenientByDefault.class);

        // then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    public static class LenientByDefault {
        @Rule
        public final RuleChain chain =
                RuleChain.outerRule(MockitoJUnit.testRule(this))
                        .around(
                                (base, description) ->
                                        new Statement() {
                                            @Override
                                            public void evaluate() throws Throwable {
                                                assertThat(MockUtil.isMock(mock)).isTrue();
                                                called.set(true);
                                                base.evaluate();
                                            }
                                        });

        @Mock public IMethods mock;

        private AtomicBoolean called = new AtomicBoolean(false);

        @Test
        public void creates_mocks_in_correct_rulechain_ordering() {
            assertThat(MockUtil.isMock(mock)).isTrue();
            assertThat(called.get()).isTrue();
        }
    }

    public static class StrictByDefault {
        @Rule
        public final RuleChain chain =
                RuleChain.outerRule(MockitoJUnit.testRule(this).strictness(Strictness.STRICT_STUBS))
                        .around(
                                (base, description) ->
                                        new Statement() {
                                            @Override
                                            public void evaluate() throws Throwable {
                                                assertThat(MockUtil.isMock(mock)).isTrue();
                                                called.set(true);
                                                base.evaluate();
                                            }
                                        });

        @Mock public IMethods mock;

        private AtomicBoolean called = new AtomicBoolean(false);

        @Test
        public void creates_mocks_in_correct_rulechain_ordering() {
            assertThat(MockUtil.isMock(mock)).isTrue();
            assertThat(called.get()).isTrue();
        }

        @Test
        public void unused_stub() throws Throwable {
            when(mock.simpleMethod()).thenReturn("1");
            assertThat(called.get()).isTrue();
        }
    }
}
