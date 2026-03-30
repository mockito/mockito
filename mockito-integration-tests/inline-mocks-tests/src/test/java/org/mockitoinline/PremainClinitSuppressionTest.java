/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for premain-based clinit suppression.
 *
 * <p>This test is run by a dedicated Gradle task that passes
 * {@code -javaagent} and {@code -Dmockito.suppress.clinit} pointing to the
 * target class below. When suppression is active, the static fields retain
 * their JVM zero-value defaults.
 */
public class PremainClinitSuppressionTest {

    /**
     * Target class whose static initializer should be suppressed by the agent.
     */
    public static class ClassWithStaticInit {
        public static String value = "initialized";
        public static int number = 42;
        public static boolean flag = true;
    }

    @Test
    public void static_fields_have_zero_defaults_when_clinit_suppressed() {
        assertThat(ClassWithStaticInit.value).isNull();
        assertThat(ClassWithStaticInit.number).isEqualTo(0);
        assertThat(ClassWithStaticInit.flag).isFalse();
    }
}
